package com.prince.hackernewsapp.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prince.hackernewsapp.R;
import com.prince.hackernewsapp.model.Comments;
import com.prince.hackernewsapp.network.RetrofitClient;
import com.prince.hackernewsapp.ui.adapter.CommentsAdapter;
import com.prince.hackernewsapp.utils.ConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsFragment extends Fragment {
    private static final String ARG_PARAM1 = "commentList";

    // TODO: Rename and change types of parameters
    private ArrayList<Integer> mParam1;
    private List<Comments> commentsList = new ArrayList<>();

    private CommentsAdapter mAdapter;

    private Unbinder unbinder;

    @BindView(R.id.rv_comments)
    RecyclerView mRecyclerView;
    private Disposable disposable;

    public CommentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CommentsFragment.
     */
    public static CommentsFragment newInstance(ArrayList<Integer> param1) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getIntegerArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new CommentsAdapter(getActivity(), commentsList);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        if (ConnectionManager.isNetworkAvailable(getActivity())) {
            fetchComments();
        } else {
            Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to make API calls and fetch comments
     */
    private void fetchComments() {
        AtomicInteger position= new AtomicInteger();
        disposable = Observable.fromIterable(mParam1)
                    .flatMap(integer -> RetrofitClient.getApiService().getComments(integer))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(comment -> {
                        if (getActivity()!=null && isAdded()) {
                            commentsList.add(comment);
                            mAdapter.notifyItemInserted(position.getAndIncrement());
                        }
                        }, Throwable::getLocalizedMessage);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable != null) disposable.dispose();
        unbinder.unbind();
    }
}
