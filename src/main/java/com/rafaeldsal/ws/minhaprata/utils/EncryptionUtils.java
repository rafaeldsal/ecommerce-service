package com.rafaeldsal.ws.minhaprata.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class EncryptionUtils {

  // Chave de 16 caracteres = 128 bits
  private static final String SECRET_KEY = "minha-chave-secr"; // 16 caracteres
  private static final String ALGORITHM = "AES";
  private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"; // Modo CBC com padding

  // Método para criptografar os dados
  public static String encrypt(String data) throws Exception {
    // Gerando a chave a partir da chave secreta
    SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);

    // Gerando o vetor de inicialização (IV) aleatório
    Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
    byte[] iv = new byte[16]; // 16 bytes para o IV
    new java.security.SecureRandom().nextBytes(iv); // Preenchendo com bytes aleatórios
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

    // Criptografando os dados
    byte[] encrypted = cipher.doFinal(data.getBytes());

    // Concatenando o IV com os dados criptografados para poder recuperar o IV na descriptografia
    byte[] encryptedDataWithIv = new byte[iv.length + encrypted.length];
    System.arraycopy(iv, 0, encryptedDataWithIv, 0, iv.length);
    System.arraycopy(encrypted, 0, encryptedDataWithIv, iv.length, encrypted.length);

    // Retornando os dados criptografados codificados em Base64
    return Base64.getEncoder().encodeToString(encryptedDataWithIv);
  }

  // Método para descriptografar os dados
  public static String decrypt(String encryptedData) throws Exception {
    // Decodificando os dados criptografados em Base64
    byte[] encryptedDataWithIv = Base64.getDecoder().decode(encryptedData);

    // Extraindo o IV dos dados criptografados
    byte[] iv = new byte[16]; // 16 bytes para o IV
    System.arraycopy(encryptedDataWithIv, 0, iv, 0, iv.length);

    // Extraindo os dados criptografados
    byte[] encrypted = new byte[encryptedDataWithIv.length - iv.length];
    System.arraycopy(encryptedDataWithIv, iv.length, encrypted, 0, encrypted.length);

    // Gerando a chave a partir da chave secreta
    SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);

    // Inicializando o modo de descriptografia
    Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

    // Descriptografando os dados
    byte[] decrypted = cipher.doFinal(encrypted);

    return new String(decrypted);
  }
}
