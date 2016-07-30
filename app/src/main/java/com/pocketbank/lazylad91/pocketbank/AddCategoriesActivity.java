package com.pocketbank.lazylad91.pocketbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pocketbank.lazylad91.pocketbank.Constant.PocketBankConstant;
import com.pocketbank.lazylad91.pocketbank.Model.Category;

import java.util.List;

public class AddCategoriesActivity extends AppCompatActivity implements OnClickListener {
static String chosenCategory;
    private CategoryAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);
        recyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    private void updateUI() {
        List<Category> categories = PocketBankConstant.categoryList;

        if (mAdapter == null) {
            mAdapter = new CategoryAdapter(categories);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mcategoryTextView;
        private ImageView mcategoryImageView;

        private Category mCategory;

        public CategoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mcategoryTextView = (TextView) itemView.findViewById(R.id.categoryName);
            mcategoryImageView = (ImageView) itemView.findViewById(R.id.categoryImage);

        }

        public void bindCategory(Category category) {
            mCategory = category;
            mcategoryTextView.setText(category.getName());
            Log.d("categoryimage", category.getImage());
            mcategoryImageView.setImageResource(getResources().getIdentifier(category.getImage(), "drawable", getPackageName()));
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
           /* Intent intent = new Intent(getActivity(), ExpenseDetail.class);
            intent.putExtra("Expense_Id",mExpense.getId());
            startActivity(intent);*/
        }


    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<Category> mCategories;

        public CategoryAdapter(List<Category> categories) {
            mCategories = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(AddCategoriesActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_category, parent, false);
            return new CategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position) {
            Category category = mCategories.get(position);
            holder.bindCategory(category);
        }

        @Override
        public int getItemCount() {
            return mCategories.size();
        }

    }


    private void returnDataToTransactionActivity(String selectedCategory) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("selectedCategory",selectedCategory);
        setResult(Activity.RESULT_OK,returnIntent);
        Log.d("Clicked", "calling finish now");
        finish();

    }


    @Override
    public void onClick(View view) {
        Log.d("Clicked", String.valueOf(view.getId()));
        EditText pressededittext = (EditText) view;
        chosenCategory = pressededittext.getText().toString();
        returnDataToTransactionActivity(chosenCategory);
    }

}


