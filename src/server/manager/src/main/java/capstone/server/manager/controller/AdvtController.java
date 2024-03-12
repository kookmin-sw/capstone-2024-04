package capstone.server.manager.controller;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard/advertisement")
public class AdvtController {

    @GetMapping
    public void getAdvtDatas(){
        return;
    }

    @GetMapping("/{id}")
    public void getAdvtData(){
        System.out.println("Request : getAdvtData() ");
        return;
    }


}
