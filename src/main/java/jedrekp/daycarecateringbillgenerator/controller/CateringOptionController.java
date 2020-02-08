package jedrekp.daycarecateringbillgenerator.controller;


import jedrekp.daycarecateringbillgenerator.entity.CateringOption;
import jedrekp.daycarecateringbillgenerator.service.CateringOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/cateringOptions")
@CrossOrigin
@RequiredArgsConstructor
public class CateringOptionController {

    private final CateringOptionService cateringOptionService;

    @GetMapping("/{cateringOptionId}")
    public ResponseEntity<CateringOption> getSingleCateringOption(@PathVariable long cateringOptionId) {
        return new ResponseEntity<>(cateringOptionService.findById(cateringOptionId), HttpStatus.OK);
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
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<CateringOption> addNewCateringOption(@RequestBody @Valid  CateringOption cateringOption) {
        return new ResponseEntity<>(cateringOptionService.saveNewCateringOption(cateringOption), HttpStatus.CREATED);
    }

    @PutMapping("/{cateringOptionId}")
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<CateringOption> editCateringOption(@PathVariable long cateringOptionId,
                                                             @RequestBody @Valid CateringOption cateringOption) {
        return new ResponseEntity<>(
                cateringOptionService.editCateringOption(cateringOption, cateringOptionId), HttpStatus.OK);
    }

}
