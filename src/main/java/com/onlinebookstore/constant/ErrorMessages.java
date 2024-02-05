package com.onlinebookstore.constant;

public class ErrorMessages {
	public static final String BOOK_NAME_EXISTED = "Book name \"%s\" is already existed";
	public static final String BOOK_NAME_EXISTED_IN_ANOTHER_BOOK = "Book name \"%s\" is already existed in another book";
	public static final String BOOK_RETURNED = "Book name \"%s\" is already returned";
	public static final String BOOK_NOT_AVAILABLE = "Book name \"%s\" is not available";
	public static final String BOOK_REQUEST_IN_PROGRESS = "Book name \"%s\" is already requested by the same user and the request is in progress";
	public static final String BOOK_ID_NOT_EXISTED = "Book ID %d is not existed";
	public static final String BOOK_ID_DELETE_NOT_ALLOWED = "Book ID %d is not allowed to be deleted as there are active requests to borrow it, please approve/reject before delete";
	public static final String BOOK_ID_BORROWED_DELETE_NOT_ALLOWED = "Book ID %d is not allowed to be deleted as this book is already borrowed. Please wait until it returned back to remove it";

	public static final String BOOK_ID_NUMBER_OF_BORROWED_COPIES_LESS_ZERO = "Book ID %d number of borrowed copies cannot be less than zero";
	public static final String USER_ID_NOT_EXISTED = "User ID %d is not existed";
	public static final String BOOK_NAME_NOT_CONTAIN = "There is no book name contain \"%s\"";
	public static final String USER_ALREADY_REGISTERED = "User name \"%s\" is already registered";
	public static final String BOOK_REQUEST_NOT_APPROVED = "Book name \"%s\" is not approved for this user";
	public static final String BOOK_REQUEST_NOT_CREATED = "Book request id %d is not requested to borrow for this user";
	public static final String BOOK_REQUEST_ALREADY_REJECTED = "Book request id %d is already rejected for this user";
	public static final String BOOK_REQUEST_ALREADY_APPROVED = "Book request id %d is already approved for this user";
	public static final String BOOK_REQUEST_NOT_PENDING = "Book request id %d is not pending request, so approve is not allowed";
	public static final String BOOK_REQUEST_WITH_BOOK_USER_NOT_CREATED = "Book request for book id %d  and user id %d is not requested to borrow for this user";
	public static final String NEW_BOOK_COPIES_EXCCEDED = "Cannot add new book \"%s\" with number of copies %d exceeded the maximum limit %d in the system ";
}
