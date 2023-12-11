package br.com.spolador.dscatalog.services.exceptions;

public class ResourceNotFoundEception extends RuntimeException{
    public ResourceNotFoundEception(String msg){
        super(msg);
    }
}
