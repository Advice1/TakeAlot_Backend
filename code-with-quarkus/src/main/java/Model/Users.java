package Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @JsonProperty("_id")
    private String _id = ObjectId.get().toHexString();
   private String Name;
    private String Surname;
    private String Email;
    private  String Password;
    private String ConfirmPassword;

}
