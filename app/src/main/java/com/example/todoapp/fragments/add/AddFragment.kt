package com.example.todoapp.fragments.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.example.todoapp.databinding.FragmentAddBinding
import com.example.todoapp.fragments.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        // Gọi hàm chọn ngày
        setupDatePickers()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupDatePickers() {
        binding.startDateEt.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val dateString = String.format("%02d/%02d/%04d", day, month + 1, year)
                    binding.startDateEt.setText(dateString)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.endDateEt.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val dateString = String.format("%02d/%02d/%04d", day, month + 1, year)
                    binding.endDateEt.setText(dateString)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add)
            insertDataToDB()
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDB() {
        val mTitle = binding.titleEt.text.toString()
        val mPriority = binding.prioritiesSpinner.selectedItem.toString()
        val mDescription = binding.descriptionEt.text.toString()
        val mStartDate = binding.startDateEt.text.toString()
        val mEndDate = binding.endDateEt.text.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)

        if (validation) {
            val newData = ToDoData(
                id = 0,
                title = mTitle,
                priority = mSharedViewModel.parsePriorityString(mPriority),
                description = mDescription,
                startDate = mStartDate,
                endDate = mEndDate
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all the fields.", Toast.LENGTH_SHORT).show()
        }
    }
}
