//package com.numo.wordapp.controller;
//
//import com.numo.wordapp.security.service.UserDetailsImpl;
//import com.numo.wordapp.service.word.FolderService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RequestMapping("/folders/")
//@RestController
//@RequiredArgsConstructor
//public class FolderController {
//    private final FolderService folderService;
//
//    @Operation(description = "폴더명 리스트를 가져온다.")
//    @GetMapping(value = "/folder")
//    public ResponseEntity<List<FolderDto.Response>> getFolderName(@AuthenticationPrincipal UserDetailsImpl user){
//        String userId = user.getUsername();
//        List<FolderDto.Response> fdto =  folderService.getByFolderName(userId).stream()
//                .map(FolderDto.Response::new)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(fdto);
//    }
//
//    @Operation(description = "폴더를 생성한다.")
//    @PostMapping(value = "/folder")
//    public ResponseEntity<FolderDto.Response> setFolder(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody FolderDto.Request fdto){
//        String userId = user.getUsername();
//        fdto.setUser_id(userId);
//        FolderDto.Response data = new FolderDto.Response(folderService.setByFolder(fdto));
//        return ResponseEntity.ok(data);
//    }
//
//    @Operation(description = "폴더를 변경한다.")
//    @PutMapping (value = "/folder/{id}")
//    public ResponseEntity<FolderDto.Response> updateFolder(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("id") int id, @RequestBody FolderDto.Request fdto){
//        String userId = user.getUsername();
//        fdto.setFolder_id(id);
//        fdto.setUser_id(userId);
//        FolderDto.Response data = new FolderDto.Response(folderService.updateByFolder(fdto));
//        return ResponseEntity.ok(data);
//    }
//
//    @Operation(description = "폴더를 삭제한다.")
//    @DeleteMapping(value="/folder/{id}")
//    public ResponseEntity<Integer> removeFolder(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("id") int id){
//        String userId = user.getUsername();
//        FolderDto.Request fdto = new FolderDto.Request();
//        fdto.setFolder_id(id);
//        fdto.setUser_id(userId);
//        int data = folderService.removeByFolder(fdto);
//        return ResponseEntity.ok(data);
//    }
//}
