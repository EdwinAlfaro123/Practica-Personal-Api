package PracticaPersonal2.PracticaPersonal2.Controller;

import PracticaPersonal2.PracticaPersonal2.Exceptions.ExceptionDatosDuplicados;
import PracticaPersonal2.PracticaPersonal2.Exceptions.ExceptionNoEncontrado;
import PracticaPersonal2.PracticaPersonal2.Model.DTO.UserDTO;
import PracticaPersonal2.PracticaPersonal2.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiUser")
public class UserController {
    @Autowired
    UserService service;

    @GetMapping("/ObtenerUsuarios")
    public List<UserDTO> obtenerDatos(){
        return service.obtenerDatos();
    }

    @PostMapping("/InsertarDatos")
    public ResponseEntity<?> nuevoDato(@Valid @RequestBody UserDTO dto, HttpServletRequest request){
        try{
            UserDTO respuesta = service.insertarDatos(dto);
            if(respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "insercion fallida",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Los datos no se pueden insertar"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "data", "respuesta"
            ));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error",
                    "message", "Error no controlado",
                    "detail" , e.getMessage()
            ));
        }
    }

    @PutMapping("/editarDatos/{id}")
    public ResponseEntity<?> Modificar(@PathVariable Long id, @Valid @RequestBody UserDTO json, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        try{
            UserDTO dto = service.actualizarDatos(id, json);
            return ResponseEntity.ok(dto);
        } catch (ExceptionNoEncontrado e) {
            return ResponseEntity.notFound().build();
        } catch (ExceptionDatosDuplicados e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "Error", "Campos duplicads",
                    "Campo", e.getCampoDuplicado()
            ));
        }
    }

    @DeleteMapping("/eliminarDatos/{id}")
    public ResponseEntity<?> eliminarDatos(@PathVariable Long id){
        try{
            if(!service.removerDatos(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("error", "Dato no encontrado")
                        .body(Map.of(
                                "Error", "NOT FOUND",
                                "Mensaje", "ID no encontrado",
                                "timestap", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "proceso completado",
                    "message", "eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al elminar",
                    "detail", e.getMessage()
            ));
        }
    }
}
