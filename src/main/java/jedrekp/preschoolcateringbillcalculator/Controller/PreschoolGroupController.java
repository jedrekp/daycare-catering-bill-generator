package jedrekp.preschoolcateringbillcalculator.Controller;

import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.preschoolcateringbillcalculator.Entity.PreschoolGroup;
import jedrekp.preschoolcateringbillcalculator.Service.PreschoolGroupService;
import jedrekp.preschoolcateringbillcalculator.Utility.JsonViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class PreschoolGroupController {

    @Autowired
    PreschoolGroupService preschoolGroupService;

    @PostMapping("/preschoolGroups")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<PreschoolGroup> addNewPreschoolGroup(@RequestBody PreschoolGroup preschoolGroup) {
        return new ResponseEntity<>(preschoolGroupService.save(preschoolGroup), HttpStatus.OK);
    }

    @GetMapping("/preschoolGroups")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Collection<PreschoolGroup>> getAllPreschoolGroups() {
        return new ResponseEntity<>(preschoolGroupService.findAll(), HttpStatus.OK);
    }
}
