package PracticaPersonal2.PracticaPersonal2.Service;

import PracticaPersonal2.PracticaPersonal2.Entity.UserEntity;
import PracticaPersonal2.PracticaPersonal2.Exceptions.ExceptionNoEncontrado;
import PracticaPersonal2.PracticaPersonal2.Model.DTO.UserDTO;
import PracticaPersonal2.PracticaPersonal2.Repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository repo;

    public List<UserDTO> obtenerDatos(){
        List<UserEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    public UserDTO insertarDatos (UserDTO dto){
        if (dto == null || dto.getUsername() == null || dto.getUsername().isEmpty()){
            throw new IllegalArgumentException("El nombre de usuario esta vacio");
        }
        try{
            UserEntity entity = ConvertirAEntity(dto);
            UserEntity guardado = repo.save(entity);
            return ConvertirADTO(guardado);
        } catch (Exception e) {
            log.error("Los datos no se pudieron insertar" + e.getMessage());
            throw new ExceptionNoEncontrado("Los datos no se pudieron insertar");
        }
    }

    public UserDTO actualizarDatos (Long id, @Valid UserDTO dto){
        UserEntity existente = repo.findById(id).orElseThrow(() -> new ExceptionNoEncontrado("El usuario no pudo ser encontrado"));

        existente.setUsername(dto.getUsername());
        existente.setEmail(dto.getEmail());
        existente.setPassword(dto.getPassword());
        UserEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean removerDatos (Long id){
        try{
            UserEntity Existe = repo.findById(id).orElse(null);
            if(Existe != null){
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se pudo encontrar el id" + id + " para eliminar.", 1);
        }
    }

    private UserDTO ConvertirADTO (UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        return dto;
    }

    private UserEntity ConvertirAEntity (UserDTO dto){
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        return entity;
    }
}
