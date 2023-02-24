package org.example.exception;

public class PedidoNaoEncontradoExcepetion extends RuntimeException {


    public PedidoNaoEncontradoExcepetion() {
        super("Pedido não encontrado.");
    }
}
