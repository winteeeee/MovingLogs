package com.ssafy.enjoytrip.features.user.adapter.in.web;

import com.ssafy.enjoytrip.features.user.adapter.in.web.response.DeleteUserResponse;
import com.ssafy.enjoytrip.features.user.adapter.in.web.response.SearchUserResponse;
import com.ssafy.enjoytrip.features.user.application.port.in.DeleteUserUseCase;
import com.ssafy.enjoytrip.features.user.application.port.in.SearchUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.prefix}/${api.version}/users")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "회원 관련 API 제공")
public class UserController {
	private final SearchUserUseCase searchUserUseCase;
	private final DeleteUserUseCase deleteUserUseCase;

	@GetMapping("/{id}")
	@Operation(summary = "회원 조회", description = "특정 아이디를 지닌 회원을 반환한다.")
	public ResponseEntity<SearchUserResponse> findById(@PathVariable String id) {
		SearchUserUseCase.Command command = UserControllerMapper.toSearchUserCommand(id);
		SearchUserUseCase.Result result = searchUserUseCase.searchUser(command);
		SearchUserResponse response = UserControllerMapper.toSearchUserResponse(result);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "회원 삭제", description = "특정 아이디를 지닌 회원을 탈퇴시킨다. (soft delete)")
	public ResponseEntity<DeleteUserResponse> delete(@PathVariable String id) {
		DeleteUserUseCase.Command command = UserControllerMapper.toDeleteUserCommand(id);
		DeleteUserUseCase.Result result = deleteUserUseCase.deleteUser(command);
		DeleteUserResponse response = UserControllerMapper.toDeleteUserResponse(result);
		return ResponseEntity.ok(response);
	}
}
