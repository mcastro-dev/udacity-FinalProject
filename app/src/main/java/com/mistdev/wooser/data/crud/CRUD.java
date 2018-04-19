package com.mistdev.wooser.data.crud;

import android.content.Context;
import android.net.Uri;

import com.mistdev.wooser.data.crud.asyncTasks.CreateAsyncTask;
import com.mistdev.wooser.data.crud.asyncTasks.DeleteAsyncTask;
import com.mistdev.wooser.data.crud.asyncTasks.ReadAsyncTask;
import com.mistdev.wooser.data.crud.asyncTasks.UpdateAsyncTask;
import com.mistdev.wooser.data.models.IParser;

/**
 * Created by mcastro on 08/03/17.
 */

public class CRUD<T extends IParser<T>> {

    public static final int INVALID_ID = -1;

    private Class<T> tClass;
    private Context mContext;
    private Uri mUri;

    CRUD(Context context, Uri uri, Class<T> tClass) {
        mContext = context;
        mUri = uri;
        this.tClass = tClass;
    }

    public void create(T obj, final CreateCallback callback) throws Exception {

        new CreateAsyncTask<T>(mContext.getContentResolver(), mUri, obj) {
            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);
                if(callback == null) {
                    return;
                }
                //Fail
                if(id == INVALID_ID) {
                    callback.onCreateFail();
                    return;
                }
                //Succeed
                callback.onCreateSuccess(id);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                if(callback == null) {
                    return;
                }
                callback.onCreateFail();
            }
        }.execute();
    }

    public void read(long id, final ReadCallback<T> callback) throws Exception {

        new ReadAsyncTask<T>(mContext.getContentResolver(), mUri, tClass) {
            @Override
            protected void onPostExecute(T obj) {
                super.onPostExecute(obj);
                if(callback == null) {
                    return;
                }

                //Fail
                if(obj == null) {
                    callback.onReadFail();
                    return;
                }
                //Success
                callback.onReadSuccess(obj);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                if(callback == null) {
                    return;
                }
                callback.onReadFail();
            }
        }.execute(id);
    }

    public void update(long id, T obj, final UpdateCallback callback) throws Exception {

        new UpdateAsyncTask<T>(mContext.getContentResolver(), mUri, obj) {

            @Override
            protected void onPostExecute(Integer updateCount) {
                super.onPostExecute(updateCount);
                if(callback == null) {
                    return;
                }

                //Fail
                if(updateCount == 0) {
                    callback.onUpdateFail();
                    return;
                }
                //Success
                callback.onUpdateSuccess(updateCount);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                if(callback == null) {
                    return;
                }
                callback.onUpdateFail();
            }

        }.execute(id);
    }

    public void delete(long id, final DeleteCallback callback) throws Exception {

        new DeleteAsyncTask<T>(mContext.getContentResolver(), mUri) {

            @Override
            protected void onPostExecute(Integer deleteCount) {
                super.onPostExecute(deleteCount);
                if(callback == null) {
                    return;
                }

                //Fail
                if(deleteCount == 0) {
                    callback.onDeleteFail();
                    return;
                }
                //Success
                callback.onDeleteSuccess(deleteCount);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                if(callback == null) {
                    return;
                }
                callback.onDeleteFail();
            }

        }.execute(id);
    }


    /* CALLBACKS
     * ----------------------------------------------------------------------------------------------*/
    public interface CreateCallback {
        void onCreateSuccess(long objId);
        void onCreateFail();
    }

    public interface ReadCallback<T> {
        void onReadSuccess(T obj);
        void onReadFail();
    }

    public interface UpdateCallback {
        void onUpdateSuccess(int updateCount);
        void onUpdateFail();
    }

    public interface DeleteCallback {
        void onDeleteSuccess(int deleteCount);
        void onDeleteFail();
    }
}
