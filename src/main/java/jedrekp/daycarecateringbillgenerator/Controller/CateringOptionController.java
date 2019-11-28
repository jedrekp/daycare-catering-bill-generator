package jedrekp.daycarecateringbillgenerator.Controller;


import jedrekp.daycarecateringbillgenerator.Entity.CateringOption;
import jedrekp.daycarecateringbillgenerator.Service.CateringOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/cateringOptions")
@CrossOrigin
public class CateringOptionController {

    @Autowired
    CateringOptionService cateringOptionService;

    @GetMapping("/{id}")
    public ResponseEntity<CateringOption> getSingleCateringOption(@PathVariable Long id) {
        return new ResponseEntity<>(cateringOptionService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<CateringOption>> getAllCateringOptions() {
        return new ResponseEntity<>(cateringOptionService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = "disabled")
    public ResponseEntity<Collection<CateringOption>> getAllCateringOptionsByDisabled(@RequestParam boolean disabled) {
        return new ResponseEntity<>(cateringOptionService.findAllByDisabled(disabled), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CateringOption> addNewCateringOption(@RequestBody CateringOption cateringOption) {
        return new ResponseEntity<>(cateringOptionService.saveNewCateringOption(cateringOption), HttpStatus.CREATED);
    }

    @PutMapping("/{cateringOptionId}")
    public ResponseEntity<CateringOption> editCateringOption(@PathVariable Long cateringOptionId,
                                                             @RequestBody CateringOption cateringOption) {
        return new ResponseEntity<>(
                cateringOptionService.editCateringOption(cateringOption, cateringOptionId), HttpStatus.OK);
    }

}
