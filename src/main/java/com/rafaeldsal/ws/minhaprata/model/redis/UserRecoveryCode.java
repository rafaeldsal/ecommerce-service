package com.rafaeldsal.ws.minhaprata.model.redis;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@RedisHash("userRecoveryCode")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecoveryCode {

  @Id
  private String id;

  @Indexed
  @Email
  private String email;

  @NotBlank(message = "campo obrigatório")
  private String name;

  private String code;

  private LocalDateTime creationDate;
}
