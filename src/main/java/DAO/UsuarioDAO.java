package DAO;
import dondeestas.Usuario;


public interface UsuarioDAO<T> extends GenericDAO<T>{
    public T getByDni(int dni);
    public T getByEmail(String email);
}
