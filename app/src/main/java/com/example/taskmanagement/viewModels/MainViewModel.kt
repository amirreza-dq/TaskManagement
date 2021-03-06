package com.example.taskmanagement.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.data.entities.SubTask
import com.example.taskmanagement.data.entities.Task
import com.example.taskmanagement.data.entities.TaskMember
import com.example.taskmanagement.data.entities.User
import com.example.taskmanagement.data.entities.relations.TaskWithSubTasks
import com.example.taskmanagement.data.entities.relations.TaskWithTaskMembers
import com.example.taskmanagement.repositories.TaskManagementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskManagementRepository
) : ViewModel() {


    var allTasksMinusNewestTask: LiveData<List<Task>> = repository.getAllTasksMinusNewestTask()
    var allTasks: LiveData<List<Task>> = repository.getAllTasks()
    var allUsers: LiveData<List<User>> = repository.getAllUsers()
    var allTaskMembers: LiveData<List<TaskMember>> = repository.getAllTaskMembers()
    var newestTask: LiveData<Task> = repository.getNewestTask()

    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTask(task)
    }

    fun insertSubTask(subTask: SubTask) = viewModelScope.launch() {
        repository.insertSubTask(subTask)
    }

    fun insertTaskMember(taskMember: TaskMember) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTaskMember(taskMember)
    }

    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertUser(user)
    }

    fun updateUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateUser(user)
    }

    fun updateTask(task_progression: Float, taskId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTask(task_progression, taskId)
    }

    fun updateSubTaskStatus(subTask: SubTask) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateSubTaskStatus(subTask)
    }

    fun getTaskWithSubTasks(taskId: Int): LiveData<List<TaskWithSubTasks>> =
        repository.getTaskWithSubTasks(taskId)


    fun getTaskWithTaskMembers(taskId: Int): LiveData<List<TaskWithTaskMembers>> =
        repository.getTaskWithTaskMembers(taskId)

}

