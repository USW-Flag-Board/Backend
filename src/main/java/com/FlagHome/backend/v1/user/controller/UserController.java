import com.FlagHome.backend.v1.user.service.UserSecurityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class UserController {
    private UserSecurityService userService;

    @GetMapping("/login")
    public String login() {
        return "login";  //리턴값 뭐라고 하죠...?
    }


}