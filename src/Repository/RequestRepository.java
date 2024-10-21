package Repository;

import BookStoreModel.Book;
import BookStoreModel.Request;
import DI.Inject;
import Dao.GenericDaoImpl;
import Status.BookStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class RequestRepository extends GenericDaoImpl<Request, Integer> {
    @Inject
    private Connection connection;
    private final BookRepository bookRepository;
    public RequestRepository(Connection connection, BookRepository bookRepository){
        super(connection);
        this.bookRepository=bookRepository;
    }

    @Override
    public void delete(int requestId) {
        String sql = getDeleteSql();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setIdParameter(statement, requestId);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("No rows deleted, invalid requestId: " + requestId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete request with requestId: " + requestId, e);
        }
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
        int bookId = resultSet.getInt("bookId");
        Book book = bookRepository.getById(bookId);
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
        statement.setInt(2, request.getRequestId());
    }

    @Override
    protected String getDeleteSql(){
        return "DELETE FROM Request WHERE requestId = ?";
    }
}
