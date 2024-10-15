package Repository;

import BookStoreModel.Book;
import BookStoreModel.Request;
import Dao.GenericDaoImpl;
import Status.BookStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class RequestRepository extends GenericDaoImpl<Request, Integer> {

    public RequestRepository(Connection connection){
        super(connection);
    }

    @Override
    protected String getCreateSql(){
        return "INSERT INTO Request(bookId) VALUES (?)";
    }

    @Override
    protected void populateCreateStatement(PreparedStatement statement, Request request) throws SQLException{
        statement.setInt(1, request.getBook().getBookId());
    }

    @Override
    protected void setId(Request request, Integer id){
        request.setRequestId(id);
    }

    @Override
    protected String getSelectByIdSql(){
        return "SELECT * FROM Request WHERE requestId = ?";
    }

    @Override
    protected void setIdParameter(PreparedStatement statement, Integer id) throws SQLException{
        statement.setInt(1, id);
    }

    @Override
    protected Request mapResultSetToEntity(ResultSet resultSet) throws SQLException{
        int bookId=resultSet.getInt("bookId");
        BookRepository bookRepository=new BookRepository(connection);
        Book book=bookRepository.getById(bookId);
        return new Request(book);
    }

    @Override
    protected String getSelectAllSql(){
        return "SELECT * FROM Request";
    }

    @Override
    protected String getUpdateSql(){
        return "UPDATE Request SET requestId = ?";
    }

    @Override
    protected void populateUpdateStatement(PreparedStatement statement, Request request) throws SQLException {
        statement.setInt(1, request.getBook().getBookId());
    }

    @Override
    protected String getDeleteSql(){
        return "DELETE FROM Request WHERE requestId = ?";
    }
}
