package ru.khasang.cachoeira.commands;

import java.util.Stack;

public class CommandControl {
    private static CommandControl instance;
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    private CommandControl() {
    }

    public static CommandControl getInstance() {
        if (instance == null) {
            instance = new CommandControl();
        }
        return instance;
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
}
