package com.atharva.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.atharva.configuration.FirebaseInitialization;
import com.atharva.model.Note;
import com.google.api.core.*;
import com.google.cloud.firestore.*;

public class NotesDao {

  public static Firestore db;

  static {
    db = FirebaseInitialization.getFireStore();
  }

  public void addData(String collection, String document, Note data) throws InterruptedException, ExecutionException{
    DocumentReference docRef = db.collection(collection).document(document);
    ApiFuture<WriteResult> result = docRef.set(data);
    result.get();
  }

  public List<Note> getDataList(String collection, String userName) throws InterruptedException, ExecutionException{
    try{
      CollectionReference colRef = db.collection(collection);
      Query query = colRef.whereEqualTo("userName",userName);
      ApiFuture<QuerySnapshot> querySnapshotFuture = query.get();
      QuerySnapshot querySnapshot = querySnapshotFuture.get();
      List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
      List<Note> noteList = new ArrayList<>();

      for(QueryDocumentSnapshot document : documents){
        Note note = document.toObject(Note.class);
        noteList.add(note);
      }

      return noteList;

    } catch(Exception e){
      e.printStackTrace();
      throw e;
    }
  }
}
