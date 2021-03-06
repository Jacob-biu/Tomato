package cn.njupt.tomato.Fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.njupt.tomato.Dao.ToDoDao;
import cn.njupt.tomato.R;
import cn.njupt.tomato.SpacesItemDecoration;
import cn.njupt.tomato.Adapter.TodoRecyclerViewAdapter;
import cn.njupt.tomato.Utils.NetWorkUtils;
import cn.njupt.tomato.Utils.TodoItemTouchHelperCallback;
import cn.njupt.tomato.Utils.ToDoUtils;
import cn.njupt.tomato.Bean.Todos;
import cn.njupt.tomato.Bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;


public class TodoFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Todos> todosList = new ArrayList<>();
    private TodoRecyclerViewAdapter todoRecyclerViewAdapter;
    private User currentUser;
    private ItemTouchHelper mItemTouchHelper;
    private List<Todos> localTodo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(NetWorkUtils.isNetworkConnected(getContext())) {
            try{
                if (User.getCurrentUser(User.class) != null){
                    currentUser = BmobUser.getCurrentUser(User.class);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo, container, false);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todosList,getActivity());
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new SpacesItemDecoration(0));
        recyclerView.setAdapter(todoRecyclerViewAdapter);
        ItemTouchHelper.Callback callback = new TodoItemTouchHelperCallback(todoRecyclerViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        setDbData();
//                        setNetData();
//                    }
//                });
//            }
//        }).start();
        setDbData();
        setNetData();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume(){
        setDbData();
        todoRecyclerViewAdapter.notifyDataSetChanged();
        super.onResume();

    }




    private void setDbData(){
        localTodo = ToDoUtils.getAllTodos(getContext());
        if (localTodo.size() > 0) {
            setListData(localTodo);
        }
    }
    /**
     * ??????????????????list
     */
    private void setNetData() {

        if (NetWorkUtils.isNetworkConnected(getContext())){
            if (currentUser != null){
                // ???????????????????????????????????????????????????????????????????????????????????????????????????
                ToDoUtils.getNetAllTodos(getContext(), currentUser, new ToDoUtils.GetTodosCallBack() {
                    @Override
                    public void onSuccess(List<Todos> listTodos) {
                        if (localTodo.size() < listTodos.size()){
                            new ToDoDao(getContext()).clearAll();
                            if ( listTodos!= null){
                                setListData(listTodos);
                                new ToDoDao(getContext()).saveAll(listTodos);
                            }
                        }
                    }

                    @Override
                    public void onError(int errorCode, String msg) {

                    }
                });

            }
        }

    }

    /**
     * ??????list??????
     */
    private void setListData(List<Todos> newList) {
        todosList.clear();
        todosList.addAll(newList);
        todoRecyclerViewAdapter.notifyDataSetChanged();
    }


}
