package com.example.taskmanagement.ui.fragments.onMainActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagement.R
import com.example.taskmanagement.adapters.SubTaskRecyclerViewAdapter
import com.example.taskmanagement.adapters.TaskMembersRecyclerViewAdapter
import com.example.taskmanagement.data.entities.SubTask
import com.example.taskmanagement.data.entities.User
import com.example.taskmanagement.databinding.FragmentTaskDetailBinding
import com.example.taskmanagement.utils.RecyclerViewMarginItemDecoration
import com.example.taskmanagement.utils.setupRecyclerView
import com.example.taskmanagement.utils.shortSnackBar
import com.example.taskmanagement.viewModels.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TaskDetailFragment : BaseFragment() {

    private val taskMembersRecyclerAdapter = TaskMembersRecyclerViewAdapter()
    private val subTaskRecyclerAdapter =  SubTaskRecyclerViewAdapter()
    private lateinit var binding: FragmentTaskDetailBinding
    private val viewModel: MainViewModel by viewModels()
    private val args: TaskDetailFragmentArgs by navArgs()
    var currentTaskId: Int? = null
    private var users = listOf<User>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_detail, container, false)

        binding.fragmentTaskDetail = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupToolbar(view)
        initTaskDetailsFields()
        getAllUsers()
        showTaskMembers()
        showSubTasks()
        setOnSubTaskItemClickListener()
    }

    private fun showSubTasks() {
        viewModel.getTaskWithSubTasks(currentTaskId!!).observe(viewLifecycleOwner, Observer {
            it.forEach {
                if (it.subTasks.isEmpty()) {
                    binding.notAnyTaskToShow.visibility = View.VISIBLE
                } else {
                    binding.notAnyTaskToShow.visibility = View.INVISIBLE
                    subTaskRecyclerAdapter.differ.submitList(it.subTasks)

                    initSubTaskRecyclerView()
                }
            }

        })
    }

    private fun initMembersRecyclerView() {
        binding.taskMembersRecyclerView.setupRecyclerView(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            taskMembersRecyclerAdapter,
            RecyclerViewMarginItemDecoration(0, 0, 0, -30)
        )
    }

    private fun initSubTaskRecyclerView() {

        binding.subTaskRecyclerView.setupRecyclerView(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false),
            subTaskRecyclerAdapter,
            RecyclerViewMarginItemDecoration(0, 0, 0, 1)
        )
    }

    private fun getAllUsers() {
        viewModel.allUsers.observe(viewLifecycleOwner, Observer {
            users = it
        })
    }

    private fun initTaskDetailsFields() {
        val task = args.task
        currentTaskId = task.taskId

        binding.taskTitle.text = task.taskTitle
        binding.shortDescription.text = task.shortDescription
        binding.taskDescription.text = task.longDescription
    }

    private fun showTaskMembers() {
        viewModel.getTaskWithUsers(currentTaskId!!).observe(viewLifecycleOwner, Observer {
            it.forEach {
                if (it.users.isEmpty()) {
                    binding.notAnyMemberJoined.visibility = View.VISIBLE
                } else {
                    binding.taskMembersRecyclerView.visibility = View.VISIBLE
                    binding.notAnyMemberJoined.visibility = View.INVISIBLE
                    taskMembersRecyclerAdapter.differ.submitList(it.users)

                    initMembersRecyclerView()
                }
            }

        })
    }

    @SuppressLint("SetTextI18n", "CutPasteId")
    private fun setupToolbar(view: View) {

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.taskDetailFragment) {

                toolbarTitle.text = "Task Details"

                toolbarBackBtn.setOnClickListener {
                    Navigation.findNavController(it).popBackStack()
                }
            }

        }

    }

    fun onAddMemberClickListener() {
        AddMemberDialog(
            users,
            requireContext(),
            object : AddMemberToTaskDialogListener {
                override fun onAddButtonClicked(usersList: List<User>) {
                    addTaskItToMembersAndUpdateUsers(usersList)
                }
            }).show()
    }

    private fun addTaskItToMembersAndUpdateUsers(usersList: List<User>) {
        usersList.forEach { user ->
            user.apply {
                taskId = currentTaskId
            }
            viewModel.updateUser(user)
        }
    }

    fun createNewSubTaskBtnClickListener(view: View) {
        binding.apply {
            if (this.insertNewSubTaskEditText.text.isNullOrBlank()) {
                changeBackgroundLayoutToDangerousLayout(this.insertNewSubTaskEditText)
                view.shortSnackBar("Pleas Enter SUb Task Title...")
            } else {
                clearDangerBackground(this.insertNewSubTaskEditText)
                insertTaskToDatabase(view)

                binding.insertNewSubTaskEditText.text?.clear()
            }
        }
    }

    private fun changeBackgroundLayoutToDangerousLayout(view: TextInputEditText) {
        view.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.text_input_edit_text_danger_bg
        )
    }

    private fun clearDangerBackground(view: TextInputEditText) {
        view.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.gray_bg
        )
    }

    private fun insertTaskToDatabase(view: View) {

        val inputSubTask = binding.insertNewSubTaskEditText.text.toString()

        val subTask = SubTask(
            0,
            inputSubTask,
            false,
            currentTaskId!!
        )

        viewModel.insertSubTask(subTask)
    }

    private fun setOnSubTaskItemClickListener() {
        subTaskRecyclerAdapter.setOnItemClickListener {

            when(it.isCompleted){
                false ->{
                    viewModel.updateSubTaskStatus(it.apply {
                        this.isCompleted = true
                    })
                }
                true ->{
                    viewModel.updateSubTaskStatus(it.apply {
                        this.isCompleted = false
                    })
                }
            }
        }
    }
}