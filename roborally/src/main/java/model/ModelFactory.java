package model;

import networking.User;

public class ModelFactory {

    private DataModel dataModel;
    public networking.User player;

    /**
     * ModelFactory(User)
     * Constructor of the ModelFactory
     * @param player
     * @author Thomas Richter
     */
    public ModelFactory(User player) {
        this.player = player;
    }

    /**
     * getDataModel()
     * This method returns a new DataModel, the Model of MVVM Pattern, in case no DataModel is yet in existence.
     * @return DataModel
     * @author Thomas Richter
     */

    public DataModel getDataModel() {
        if (dataModel == null) {
            dataModel = new DataModelManager(player);
            player.setDataModel((DataModelManager) dataModel);
        }
        return dataModel;
    }














}
