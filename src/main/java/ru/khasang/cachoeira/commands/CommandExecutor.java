package ru.khasang.cachoeira.commands;

import java.util.Stack;

public class CommandExecutor {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    public CommandExecutor() {
    }

    public void execute(Command command) {
        redoStack.clear();
        undoStack.push(command);
        command.execute();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            redoStack.push(command);
            command.undo();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            undoStack.push(command);
            command.execute();
        }
    }

    public boolean isChanged() {
        return !undoStack.isEmpty();
    }

    public void clearLists() {
        undoStack.clear();
        redoStack.clear();
    }
}
