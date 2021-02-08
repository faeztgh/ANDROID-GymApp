package ir.faez.gymapp.data.db.DAO;

public interface DbResponse<T> {
    void onSuccess(T t);

    void onError(Error error);

}
