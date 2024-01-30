package com.example.taskmanager.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.Task

const val TAG = "TaskView"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskView(
    task: Task = Task(id=0),
    //if it or another item has longClicked-selected
    selectedMode: Boolean = false,
    checkState: Boolean = false,
    //changeSelectofItem
    onCheckedChange: (Boolean) -> Unit = {},
    //click to add or change Note
    onClicked: () -> Unit = {},
    //change selectable mode
    onLongClicked: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .height(80.dp)
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .combinedClickable(
                onClick = {
                    if (selectedMode) {
                        onLongClicked()
                    } else {
                        onClicked()
                    }
                },
                onLongClick = {
                    onLongClicked()
                    //    openDialog.value = true
                }
            ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(Color.White)

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = task.title,
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 15.dp)
            )
            if (selectedMode) {
//padding?
                Checkbox(
                    modifier = Modifier
                        .fillMaxHeight(),
                   // colors = CheckboxDefaults.colors(uncheckedColor = Color.White),
                    checked = checkState,
                    onCheckedChange = {
                        onCheckedChange(it)
                    }
                )
            } else {
                Text(
                    text = task.completionTime,
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 15.dp),

                    textAlign = TextAlign.End
                )
            }

        }

    }

}

@Preview
@Composable
fun TaskViewPreview() {
    Column {
        TaskView()
        TaskView(
            selectedMode = true,
            checkState = true
        )
        TaskView(
            selectedMode = true,
        )
    }
}


@Composable
fun ItemOld() {
    androidx.compose.material.Card(
        modifier = Modifier
            .height(80.dp)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 5.dp
    ) {
        Row(
            Modifier
                .padding(24.dp)
                .fillMaxSize()

        ) {
            Text(
                text = "item.toString()",
                modifier = Modifier
                    .weight(3f)
            )

            Text(
                text = "item.getTime()",
                modifier = Modifier
                    .weight(2f)
            )

        }
    }
}

@Preview
@Composable
fun ItemOldPreview() {
    ItemOld()
}


/*
.pointerInput(Unit) {
    detectTapGestures {
        Log.e(TAG,"just tap")
    }

    detectDragGesturesAfterLongPress { _, _ ->
        Log.e(TAG,"just long tap")
    }
}*/
