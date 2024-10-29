package ConsoleUI;

import javax.transaction.SystemException;

public interface IAction {
    void execute() throws SystemException;
}
