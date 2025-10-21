package com.example.todoapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.example.todoapp.databinding.FragmentUpdateBinding
import com.example.todoapp.data.viewmodel.SharedViewModel
import com.example.todoapp.fragments.list.NotificationHelper

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args
        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmItemRemoval() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setPositiveButton("YES") { _, _ ->
                mToDoViewModel.deleteItem(args.currentItem)
                Toast.makeText(
                    requireContext(),
                    "Successfully Removed: '${args.currentItem.title}'",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            setNegativeButton("NO") { _, _ -> /*DO NOTHING*/ }
            setTitle("Delete '${args.currentItem.title}'")
            setMessage("Are you sure you want to remove: '${args.currentItem.title}'?")
            create()
        }
        alertDialog.show()
    }

    private fun updateItem() {
        val title = binding.titleEt.text.toString()
        val desc = binding.descriptionEt.text.toString()
        val priority = mSharedViewModel.parsePriorityString(binding.prioritiesSpinner.selectedItem.toString())
        val mStartDate = binding.startDateEt.text.toString()
        val mEndDate = binding.endDateEt.text.toString()
        if(mSharedViewModel.verifyDataFromUser(title, desc)) {
            val updatedItem = ToDoData(
                id = args.currentItem.id,
                title = title,
                priority = priority,
                description = desc,
                startDate = mStartDate,
                endDate = mEndDate
            )
            if (args.currentItem.endDate != mEndDate) {
                NotificationHelper.resetNotificationStatus(requireContext(), args.currentItem.id)
            }
            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(
                requireContext(),
                "Successfully Updated!",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill out all the fields.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}