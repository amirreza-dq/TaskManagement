package com.example.taskmanagement.ui.fragments.onMainActivity

import com.example.taskmanagement.data.entities.User


interface AddMemberToTaskDialogListener {

    fun onAddButtonClicked(item: User)
}