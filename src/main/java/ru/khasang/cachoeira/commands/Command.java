package ru.khasang.cachoeira.commands;

public interface Command {
    void execute();

    void undo();

    void redo();
}