package br.com.spolador.dscatalog.services.exceptions;

public class EntityNotFoundEception extends RuntimeException{
    public EntityNotFoundEception(String msg){
        super(msg);
    }
}
