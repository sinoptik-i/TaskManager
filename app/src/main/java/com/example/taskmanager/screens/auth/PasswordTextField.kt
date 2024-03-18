package com.example.taskmanager.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanager.screens.support.IOTextField

@Composable
fun PasswordTextField(
    text: MutableState<String>,
    labelText: String = "",
    hintText: String = ""
) {
    val visualTransformation = remember {
        mutableStateOf<VisualTransformation>(PasswordVisualTransformation())
    }

    Column(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .border(
                BorderStroke(
                    1.dp,
                    Color.Blue
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp)

    )
    {
        IOTextField(
            text = text,
            labelText = labelText,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = visualTransformation.value,
            trailingIcon = {
                Icon(
                    Icons.Rounded.Info, contentDescription = "see",
                    modifier = Modifier.clickable {
                        if (visualTransformation.value == PasswordVisualTransformation()) {
                            visualTransformation.value = VisualTransformation.None
                        } else {
                            visualTransformation.value = PasswordVisualTransformation()
                        }
                    })
            },
            singleLine = true
        )
        TextHint(
            text=hintText)
    }
}

@Composable
fun TextHint(
    text: String = ""
) {
    if (text != "") {
        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            text = text,
            fontSize = 12.sp,
            style = TextStyle(Color.Gray)
        )
    }
}


@Preview
@Composable
fun PasswordTextFieldPreview() {
    val text = remember { mutableStateOf("Password") }
    PasswordTextField(
        text = text,
        labelText = "Password",
        hintText = "unacceptable!!"
    )
}
