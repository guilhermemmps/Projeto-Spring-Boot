package org.example.rest.controller;

import org.example.exception.PedidoNaoEncontradoExcepetion;
import org.example.exception.RegraNegocioException;
import org.example.rest.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    //@ResponseBody
    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleRegraNegovioException(RegraNegocioException exception){
        String messageErro = exception.getMessage();
        return new ApiErrors(messageErro);
    }

    @ExceptionHandler(PedidoNaoEncontradoExcepetion.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handlePedidoNotFoundException(PedidoNaoEncontradoExcepetion excepetion){
        return new ApiErrors(excepetion.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMethodNotVaidException(MethodArgumentNotValidException exception){
        List<String> list = exception.getBindingResult().getAllErrors().stream().map(erro -> erro.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErrors(list);
    }


}
