package br.com.spolador.dscatalog.controllers.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class StantardError {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
