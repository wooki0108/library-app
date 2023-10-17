package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository,
            UserLoanHistoryRepository userLoanHistoryRepository, UserRepository userRepository) {

        this.bookRepository = bookRepository;
        this.userLoanHistoryRepository = userLoanHistoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveBook(BookCreateRequest request) {
        bookRepository.save(new Book(request.getName()));
    }

    @Transactional
    public void loanBook(BookLoanRequest request) {
        // 1. 책 정보를 가져온다.
        Book book = bookRepository.findByName(request.getBookName())
                .orElseThrow(IllegalArgumentException::new);

        // 2. 대출기록 정보를 확인해서 대출중인지 확인
        // 3. 확인했는데 대출 중이면 예외를 발생
        if (userLoanHistoryRepository.existsByBookNameAndIsReturn(book.getName(), false)) {
            throw new IllegalArgumentException("이미 대출중인 책 입니다.");
        }

        // 4. 유저 정보를 가져온다.
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);

        user.loanBook(book.getName());

        // 5. 유저 정보와 책 정보를 기반으로 UserLoanHistory를 저장
//        userLoanHistoryRepository.save(new UserLoanHistory(user.getId(), book.getName(), false));
        // 처음 빌리면 무조건 false

        // cascade 옵션으로 지워도된다.
//        userLoanHistoryRepository.save(new UserLoanHistory(user, book.getName()));
    }

    @Transactional
    public void returnBook(BookReturnRequest request) {
        // 1. 유저 찾기
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);

        // 2. 유저 id와 대출기록 찾기
//        UserLoanHistory history = userLoanHistoryRepository.findByUserIdAndBookName(
//                user.getId(), request.getBookName()).orElseThrow(IllegalArgumentException::new);

        // 지연 로딩(Lazy Loading) -> OneToMany 의 fetch 옵션
        // 영속성 컨텍스트 안에서만 발생
        // 연결되어 있는 객체를 꼭 필요한 순간에만 가져온다
        System.out.println("Hello");

        // Q1 그래서 연관관계 사용하면 무엇이 좋은데?
        /**
         *  1. 각자의 역할에 집중(응집성 상승)
         *  2. 코드 이해하기 쉬워짐
         */

        // Q2 연관관계 사용히는 것이 항상 좋나?
        /**
         *  그거는 아닌뎁쇼 !
         *
         *  1. 성능상의 문제 이슈
         *  2. 도메인 간의 복잡한 연결로 인해 시스템을 파악하기 어려워짐
         *  3. 다른곳까지 영향 발생 가능성
         */

        user.returnBook(request.getBookName());

        // 3. 대출기록을 반납처리 해주기
//        history.doReturn();

//        save 해줄 필요 없음 -> 영속성 컨텍스트 -> 변경 감지 기능 있음 -> 자동으로 업데이트
//        userLoanHistoryRepository.save(history);

    }
}
