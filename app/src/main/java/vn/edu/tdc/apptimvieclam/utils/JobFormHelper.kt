package vn.edu.tdc.apptimvieclam.utils

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import vn.edu.tdc.apptimvieclam.R
import vn.edu.tdc.apptimvieclam.models.Job

object JobFormHelper {

    val jobTypes = listOf("Full-time", "Part-time", "Remote")

    fun setupJobTypeSpinner(context: Context, spinner: Spinner) {
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            jobTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun selectJobType(spinner: Spinner, jobType: String) {
        val index = jobTypes.indexOf(jobType)
        if (index >= 0) {
            spinner.setSelection(index)
        }
    }

    fun getSelectedJobType(spinner: Spinner): String {
        return spinner.selectedItem?.toString().orEmpty()
    }

    fun validateForm(
        tilTitle: TextInputLayout,
        edtTitle: TextInputEditText,
        tilLocation: TextInputLayout,
        edtLocation: TextInputEditText,
        tilSalary: TextInputLayout,
        edtSalary: TextInputEditText,
        tilDescription: TextInputLayout,
        edtDescription: TextInputEditText,
        jobType: String,
        context: Context
    ): Job? {
        clearErrors(tilTitle, tilLocation, tilSalary, tilDescription)

        val title = edtTitle.text?.toString()?.trim().orEmpty()
        val location = edtLocation.text?.toString()?.trim().orEmpty()
        val salary = edtSalary.text?.toString()?.trim().orEmpty()
        val description = edtDescription.text?.toString()?.trim().orEmpty()

        var isValid = true

        if (title.isEmpty()) {
            tilTitle.error = context.getString(R.string.error_job_title_required)
            isValid = false
        }

        if (location.isEmpty()) {
            tilLocation.error = context.getString(R.string.error_job_location_required)
            isValid = false
        }

        if (salary.isEmpty() || !isValidSalary(salary)) {
            tilSalary.error = context.getString(R.string.error_job_salary_invalid)
            isValid = false
        }

        if (jobType.isEmpty()) {
            isValid = false
        }

        if (description.isEmpty()) {
            tilDescription.error = context.getString(R.string.error_job_description_required)
            isValid = false
        }

        if (!isValid) return null

        return Job(
            title = title,
            location = location,
            salary = salary,
            description = description,
            jobType = jobType
        )
    }

    private fun isValidSalary(salary: String): Boolean {
        return salary.isNotBlank()
    }

    private fun clearErrors(vararg layouts: TextInputLayout) {
        layouts.forEach { it.error = null }
    }
}
