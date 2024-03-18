package com.example.taskmanager.screens.auth


interface IOTextHints {
    fun message(): String
}

enum class PasswordHints : IOTextHints {
    FEW_CHARACTERS {
        override fun message() = "Минимум 6 символов"
    },
    INVALID_CHARACTERS {
        override fun message() = "Недопустимые символы"
    },
    PASSWORDS_MISMATCH {
        override fun message() = "Пароли не совпадают"
    },
    VALID_PASSWORD {
        override fun message() = "Допустимый пароль"
    };

}

enum class EmailHints : IOTextHints {
    EXISTING_EMAIL{
        override fun message()="Такой адрес уже зарегистрирован"
    },

}