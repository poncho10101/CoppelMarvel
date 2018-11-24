package com.coppel.alfonsosotelo.coppelmarvel.ui.characterlist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.coppel.alfonsosotelo.coppelmarvel.api.MarvelApiService
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseViewModel
import com.coppel.alfonsosotelo.coppelmarvel.entities.Character
import com.coppel.alfonsosotelo.coppelmarvel.utils.*
import com.vicpin.krealmextensions.*
import io.reactivex.Completable
import io.realm.Sort

/**
 * ViewModel related to a list of Characters
 */
class CharacterListViewModel(application: Application): BaseViewModel<List<Character>>(application) {
    override var data = MutableLiveData<List<Character>>()

    init {
        data.postValue(
            Character().query{
                sort("id", Sort.ASCENDING)
                limit(10)
            }
        )
    }

    /**
     * This method loads 10 more characters in the list from the Local DB, works in Background
     * if is no more records to get in Local calls to downloadMore()
     */
    fun loadMore() {
        val lastStatus = status.value ?: DataStatus(DataState.SUCCESS)
        setStatus(DataState.LOADING)

        Completable.fromAction {
            Character().query{
                data.value?.lastOrNull()?.let {
                    greaterThan("id", it.id)
                }
                sort("id", Sort.ASCENDING)
                limit(10)
            }.takeIf { it.isNotEmpty() }?.let {
                data.addElements(it)
                status.postValue(lastStatus)
            } ?: run {
                downloadMore()
            }
        }.setDefaultSchedulers().subscribe().addToDisposables(disposables)

    }

    /**
     * This method downloads new records from the API and adds it to the list, works in Background
     * @param lastStatus after download post this status in status LiveData, SUCCESS for default
     */
    fun downloadMore(lastStatus: DataStatus = DataStatus(DataState.SUCCESS)) {
        MarvelApiService.get(getApplication())
            .getCharacters(count<Character>())
            .setDefaultSchedulers()
            .subscribe({ requestClass ->
                requestClass.getResults().takeIf { it.isNotEmpty() }?.let {
                    it.saveAll()
                    data.addElements(it)
                }

                status.postValue(lastStatus)
            }, {
                setStatus(DataState.ERROR, it.getErrorMessage(getApplication()))
            }).addToDisposables(disposables)
    }

    /**
     * This method find a Character in the list and updates with the sent in parameters
     * If character doesn't exists in the list adds to it
     * @param character character to update in the list
     */
    fun updateCharacter(character: Character) {
        Completable.fromAction {
            data.postValue(
                (data.value?.toMutableList() ?: mutableListOf()).let { characterList ->
                    characterList.indexOfFirst{ it.id == character.id }.takeIf { it != -1 }?.let {
                        characterList.set(it, character)
                    } ?: characterList.add(character)

                    characterList
                }
            )
        }.setDefaultSchedulers().subscribe().addToDisposables(disposables)
    }

    /**
     * This method find a Character in the list, deletes it from DB and removes it from the list
     * @param character character to Delete and remove from the list
     */
    fun deleteCharacter(character: Character) {
        Completable.fromAction {
            data.postValue(
                data.value?.toMutableList()?.let { characterList ->
                    characterList.indexOfFirst { it.id == character.id }
                        .takeIf { it != -1 }?.let {
                            characterList.removeAt(it)
                        }

                    Character().delete { equalTo("id", character.id) }

                    characterList
                }
            )
        }.setDefaultSchedulers().subscribe().addToDisposables(disposables)
    }
}