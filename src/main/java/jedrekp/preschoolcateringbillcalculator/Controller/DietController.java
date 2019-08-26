package jedrekp.preschoolcateringbillcalculator.Controller;


import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.preschoolcateringbillcalculator.DTO.DateInput;
import jedrekp.preschoolcateringbillcalculator.Entity.Diet;
import jedrekp.preschoolcateringbillcalculator.Repository.DietRepository;
import jedrekp.preschoolcateringbillcalculator.Service.DietService;
import jedrekp.preschoolcateringbillcalculator.Utility.JsonViewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin
public class DietController {

    @Autowired
    DietService dietService;

    @Autowired
    DietRepository dietRepository;

    @PostMapping("/diets")
    public ResponseEntity<Diet> addNewDiet(@RequestBody Diet diet) {
        return new ResponseEntity<>(dietService.save(diet), HttpStatus.OK);
    }

    @GetMapping("/diets")
    public ResponseEntity<Collection<Diet>> getDiets() {
        return new ResponseEntity<>(dietService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/diets/children/{childId}")
    @JsonView(JsonViewFilter.BasicInfo.class)
    public ResponseEntity<Diet> getDietForChild(@PathVariable Long childId, @RequestBody DateInput dateInput) {
        return new ResponseEntity<>(dietService.findDietCurrentlyAssignedToChild(childId, dateInput.getDate()), HttpStatus.OK);
    }
}
