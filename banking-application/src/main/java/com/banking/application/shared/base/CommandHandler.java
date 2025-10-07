package com.banking.application.shared.base;

/**
 * Interface base para todos os Command Handlers do sistema.
 * Implementa o padrão Command Handler para processar comandos.
 */
public interface CommandHandler<TCommand extends Command, TResult> {

    /**
     * Processa o comando e retorna o resultado.
     * 
     * @param command O comando a ser processado
     * @return O resultado do processamento
     * @throws Exception Se ocorrer erro durante o processamento
     */
    TResult handle(TCommand command) throws Exception;

    /**
     * Retorna o tipo de comando que este handler processa.
     */
    default Class<TCommand> getCommandType() {
        // Este método pode ser sobrescrito se necessário
        return null;
    }

    /**
     * Indica se este handler suporta o comando especificado.
     */
    default boolean canHandle(Command command) {
        Class<TCommand> commandType = getCommandType();
        return commandType != null && commandType.isAssignableFrom(command.getClass());
    }

    /**
     * Executa validações pré-processamento do comando.
     * Pode ser sobrescrito para validações específicas.
     */
    default void validate(TCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
    }
}