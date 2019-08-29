package jedrekp.daycarecateringbillgenerator.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.Entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.Service.DaycareGroupService;
import jedrekp.daycarecateringbillgenerator.Utility.JsonViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class DaycareGroupController {

    @Autowired
    DaycareGroupService daycareGroupService;

    @PostMapping("/daycareGroups")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<DaycareGroup> addNewDaycareGroup(@RequestBody DaycareGroup daycareGroup) {
        return new ResponseEntity<>(daycareGroupService.save(daycareGroup), HttpStatus.OK);
    }

    @GetMapping("/daycareGroups")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<DaycareGroup>> getAllDaycareGroups() {
        return new ResponseEntity<>(daycareGroupService.findAll(), HttpStatus.OK);
    }
}
