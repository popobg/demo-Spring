package fr.diginamic.hello.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service responsable de générer, parser et validés les tokens JWT
 */
@Service
public class JwtService {
    // hard-codée pour le tuto, on peut le mettre dans une variable d'env du serveur
    // qu'on ne partage pas en production ni sur GitHub
    private static final String SECRET = "357638792F423F4428472B4B6250655368566D5";
    // Durée de validité de la token = 2 minutes
    private final long EXPIRATION_TIME = 1000*60*2;

    /**
     * Extrait la ressource visée du token (souvent un username).
     * @param token token ou "jeton" JWT (header.payload.signature)
     * @return la ressource visée par le token "subject"
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait la date d'expiration des affirmations ("claims") du token.
     * @param token token JWT
     * @return date d'expiration
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Méthode hénérique permettant d'extraire un "claim" du token JWT.
     * @param token token JWT
     * @param claimsResolver fonction qui indique le "claim" à extraire
     * @return le "claim" demandé
     * @param <T> un "claim" particulier
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrait toutes les affirmations ("claims") du token JWT.
     * @param token token JWT
     * @return tous les "claims"
     */
    private Claims extractAllClaims(String token) {
        // construit un parser avec la clé secrète appropriée
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Génère la clé nécessaire à la création et validation d'un token JWT.
     * Il décode la clé "SECRET" ici (clé encodée en Base64) et la convertit en
     * clé cryptée.
     * @return clé
     */
    // Information connue uniquement du serveur, utilisée pour construire la signature du token.
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Génère un token JWT à partir du username, de la liste de "claims" créée
     * et de la clé.
     * @param username attribut username du User
     * @return token JWT
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Crée le token JWT en lui assignant des claims, un "sujet" (en utilisant le username souvent),
     * une date à laquelle le token a été créé, une date d'expiration et la clé.
     * Le token est ensuite signé et compacté pour produire un token JWT.
     * @param claims "affirmations" du token
     * @param subject "sujet" (username souvent)
     * @return token JWT
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * Vérifie si le token a expiré en comparant la date d'expiration du token
     * à la date actuelle.
     * @param token token JWT
     * @return boolean, true si le token a expiré, sinon false.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrait le "username" du token et le compare à celui de l'objet UserDetails.
     * Vérifie également si le token a expiré.
     * Renvoie si le token est valide ou non suivant ces deux paramètres.
     * @param token token JWT
     * @param userDetails objet UserDetails
     * @return boolean, true si le token est valide, sinon false
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
