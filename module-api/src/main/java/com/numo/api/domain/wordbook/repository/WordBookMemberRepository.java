package com.numo.api.domain.wordbook.repository;

import com.numo.api.domain.wordbook.dto.WordBookMemberResponseDto;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.wordbook.WordBookMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WordBookMemberRepository extends JpaRepository<WordBookMember, Long> {
    Optional<WordBookMember> findByWordBook_IdAndUser_UserId(Long wordBookId, Long userId);

    default WordBookMember findMemberByWordBookIdAndUserId(Long wordBookId, Long userId) {
        return findByWordBook_IdAndUser_UserId(wordBookId, userId).orElseThrow(
                () -> new CustomException(ErrorCode.WORD_BOOK_MEMBER_NOT_FOUND)
        );
    }

    default WordBookMember findMemberById(Long id) {
        return findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }

    @Transactional
    @Modifying
    void deleteByWordBook_Id(Long wordBookId);

    boolean existsByWordBook_IdAndUser_UserId(Long wordBookId, Long userId);

    @Query("select new com.numo.api.domain.wordbook.dto.WordBookMemberResponseDto(" +
            "wm.id," +
            "wm.user.userId," +
            "wm.user.profileImagePath," +
            "wm.user.nickname," +
            "wm.role) " +
            "from WordBookMember wm " +
            "where wm.wordBook.id = :wordBookId")
    List<WordBookMemberResponseDto> findMemberByWordBook(@Param("wordBookId") Long wordBookId);
}
