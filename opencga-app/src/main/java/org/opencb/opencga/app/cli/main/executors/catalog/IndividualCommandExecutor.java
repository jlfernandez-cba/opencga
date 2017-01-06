/*
 * Copyright 2015-2016 OpenCB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opencb.opencga.app.cli.main.executors.catalog;


import org.opencb.commons.datastore.core.ObjectMap;
import org.opencb.commons.datastore.core.Query;
import org.opencb.commons.datastore.core.QueryOptions;
import org.opencb.commons.datastore.core.QueryResponse;
import org.opencb.opencga.app.cli.main.OpencgaCommandExecutor;
import org.opencb.opencga.app.cli.main.executors.catalog.commons.AclCommandExecutor;
import org.opencb.opencga.app.cli.main.executors.catalog.commons.AnnotationCommandExecutor;
import org.opencb.opencga.app.cli.main.options.IndividualCommandOptions;
import org.opencb.opencga.catalog.db.api.IndividualDBAdaptor;
import org.opencb.opencga.catalog.exceptions.CatalogException;
import org.opencb.opencga.catalog.models.Individual;
import org.opencb.opencga.catalog.models.acls.permissions.IndividualAclEntry;

import java.io.IOException;

/**
 * Created by agaor on 6/06/16.
 */
public class IndividualCommandExecutor extends OpencgaCommandExecutor {

    private IndividualCommandOptions individualsCommandOptions;
    private AclCommandExecutor<Individual, IndividualAclEntry> aclCommandExecutor;
    private AnnotationCommandExecutor<Individual, IndividualAclEntry> annotationCommandExecutor;

    public IndividualCommandExecutor(IndividualCommandOptions individualsCommandOptions) {
        super(individualsCommandOptions.commonCommandOptions);
        this.individualsCommandOptions = individualsCommandOptions;
        this.aclCommandExecutor = new AclCommandExecutor<>();
        this.annotationCommandExecutor = new AnnotationCommandExecutor<>();
    }


    @Override
    public void execute() throws Exception {
        logger.debug("Executing individuals command line");

        String subCommandString = getParsedSubCommand(individualsCommandOptions.jCommander);
        QueryResponse queryResponse = null;
        switch (subCommandString) {
            case "create":
                queryResponse = create();
                break;
            case "info":
                queryResponse = info();
                break;
            case "search":
                queryResponse = search();
                break;
            case "update":
                queryResponse = update();
                break;
            case "delete":
                queryResponse = delete();
                break;
            case "group-by":
                queryResponse = groupBy();
                break;
            case "acl":
                queryResponse = aclCommandExecutor.acls(individualsCommandOptions.aclsCommandOptions, openCGAClient.getIndividualClient());
                break;
            case "acl-create":
                queryResponse = aclCommandExecutor.aclsCreate(individualsCommandOptions.aclsCreateCommandOptions,
                        openCGAClient.getIndividualClient());
                break;
            case "acl-member-delete":
                queryResponse = aclCommandExecutor.aclMemberDelete(individualsCommandOptions.aclsMemberDeleteCommandOptions,
                        openCGAClient.getIndividualClient());
                break;
            case "acl-member-info":
                queryResponse = aclCommandExecutor.aclMemberInfo(individualsCommandOptions.aclsMemberInfoCommandOptions,
                        openCGAClient.getIndividualClient());
                break;
            case "acl-member-update":
                queryResponse = aclCommandExecutor.aclMemberUpdate(individualsCommandOptions.aclsMemberUpdateCommandOptions,
                        openCGAClient.getIndividualClient());
                break;
            case "annotation-sets-create":
                queryResponse = annotationCommandExecutor.createAnnotationSet(individualsCommandOptions.annotationCreateCommandOptions,
                        openCGAClient.getIndividualClient());
                break;
            case "annotation-sets-all-info":
                queryResponse = annotationCommandExecutor.getAllAnnotationSets(individualsCommandOptions.annotationAllInfoCommandOptions,
                        openCGAClient.getIndividualClient());
                break;
            case "annotation-sets-search":
                queryResponse = annotationCommandExecutor.searchAnnotationSets(individualsCommandOptions.annotationSearchCommandOptions,
                        openCGAClient.getIndividualClient());
                break;
            case "annotation-sets-delete":
                queryResponse = annotationCommandExecutor.deleteAnnotationSet(individualsCommandOptions.annotationDeleteCommandOptions,
                        openCGAClient.getIndividualClient());
                break;
            case "annotation-sets-info":
                queryResponse = annotationCommandExecutor.getAnnotationSet(individualsCommandOptions.annotationInfoCommandOptions,
                        openCGAClient.getIndividualClient());
                break;
            case "annotation-sets-update":
                queryResponse = annotationCommandExecutor.updateAnnotationSet(individualsCommandOptions.annotationUpdateCommandOptions,
                        openCGAClient.getIndividualClient());
                break;
            default:
                logger.error("Subcommand not valid");
                break;
        }

        createOutput(queryResponse);
    }

    private QueryResponse<Individual> create() throws CatalogException, IOException {
        logger.debug("Creating individual");

        ObjectMap params = new ObjectMap();
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.FAMILY.key(), individualsCommandOptions.createCommandOptions.family);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.FATHER_ID.key(), individualsCommandOptions.createCommandOptions.fatherId);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.MOTHER_ID.key(), individualsCommandOptions.createCommandOptions.motherId);
        String sex = individualsCommandOptions.createCommandOptions.sex;
        if (individualsCommandOptions.createCommandOptions.sex != null) {
            try {
                params.put(IndividualDBAdaptor.QueryParams.SEX.key(), Individual.Sex.valueOf(sex));
            } catch (IllegalArgumentException e) {
                logger.error("{} not recognized as a proper individual sex", sex);
                return null;
            }
        }

        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.ETHNICITY.key(), individualsCommandOptions.createCommandOptions.ethnicity);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_COMMON_NAME.key(),
                individualsCommandOptions.createCommandOptions.speciesCommonName);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_SCIENTIFIC_NAME.key(),
                individualsCommandOptions.createCommandOptions.speciesScientificName);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_TAXONOMY_CODE.key(),
                individualsCommandOptions.createCommandOptions.speciesTaxonomyCode);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_NAME.key(),
                individualsCommandOptions.createCommandOptions.populationName);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_SUBPOPULATION.key(),
                individualsCommandOptions.createCommandOptions.populationSubpopulation);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_DESCRIPTION.key(),
                individualsCommandOptions.createCommandOptions.populationDescription);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.KARYOTYPIC_SEX.key(),
                individualsCommandOptions.createCommandOptions.karyotypicSex);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.LIFE_STATUS.key(),
                individualsCommandOptions.createCommandOptions.lifeStatus);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.AFFECTATION_STATUS.key(),
                individualsCommandOptions.createCommandOptions.affectationStatus);

        return openCGAClient.getIndividualClient().create(individualsCommandOptions.createCommandOptions.study,
                individualsCommandOptions.createCommandOptions.name, params);
    }

    private QueryResponse<Individual> info() throws CatalogException, IOException {
        logger.debug("Getting individual information");

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.putIfNotNull(QueryOptions.INCLUDE, individualsCommandOptions.infoCommandOptions.dataModelOptions.include);
        queryOptions.putIfNotNull(QueryOptions.EXCLUDE, individualsCommandOptions.infoCommandOptions.dataModelOptions.exclude);
        return openCGAClient.getIndividualClient().get(individualsCommandOptions.infoCommandOptions.individual, queryOptions);
    }

    private QueryResponse<Individual> search() throws CatalogException, IOException {
        logger.debug("Searching individuals");

        Query query = new Query();
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.STUDY.key(), individualsCommandOptions.searchCommandOptions.study);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.NAME.key(), individualsCommandOptions.searchCommandOptions.name);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.FATHER_ID.key(), individualsCommandOptions.searchCommandOptions.fatherId);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.MOTHER_ID.key(), individualsCommandOptions.searchCommandOptions.motherId);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.FAMILY.key(), individualsCommandOptions.searchCommandOptions.family);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SEX.key(), individualsCommandOptions.searchCommandOptions.sex);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.ETHNICITY.key(), individualsCommandOptions.searchCommandOptions.ethnicity);
        // TODO: Remove these 2 deprecated parameters in future release
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_COMMON_NAME.key(),
                individualsCommandOptions.searchCommandOptions.species);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_NAME.key(),
                individualsCommandOptions.searchCommandOptions.population);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_COMMON_NAME.key(),
                individualsCommandOptions.searchCommandOptions.speciesCommonName);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_TAXONOMY_CODE.key(),
                individualsCommandOptions.searchCommandOptions.speciesTaxonomyCode);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_SCIENTIFIC_NAME.key(),
                individualsCommandOptions.searchCommandOptions.speciesScientificName);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_NAME.key(),
                individualsCommandOptions.searchCommandOptions.populationName);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_SUBPOPULATION.key(),
                individualsCommandOptions.searchCommandOptions.populationSubpopulation);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_DESCRIPTION.key(),
                individualsCommandOptions.searchCommandOptions.populationDescription);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.KARYOTYPIC_SEX.key(),
                individualsCommandOptions.searchCommandOptions.karyotypicSex);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.LIFE_STATUS.key(), individualsCommandOptions.searchCommandOptions.lifeStatus);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.AFFECTATION_STATUS.key(),
                individualsCommandOptions.searchCommandOptions.affectationStatus);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.VARIABLE_SET_ID.key(),
                individualsCommandOptions.searchCommandOptions.variableSetId);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.ANNOTATION.key(),
                individualsCommandOptions.searchCommandOptions.annotation);
        query.putIfNotEmpty(IndividualDBAdaptor.QueryParams.ANNOTATION_SET_NAME.key(),
                individualsCommandOptions.searchCommandOptions.annotationSetName);

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.putIfNotEmpty(QueryOptions.INCLUDE, individualsCommandOptions.searchCommandOptions.dataModelOptions.include);
        queryOptions.putIfNotEmpty(QueryOptions.EXCLUDE, individualsCommandOptions.searchCommandOptions.dataModelOptions.exclude);
        queryOptions.putIfNotEmpty(QueryOptions.SKIP, individualsCommandOptions.searchCommandOptions.numericOptions.skip);
        queryOptions.putIfNotEmpty(QueryOptions.LIMIT, individualsCommandOptions.searchCommandOptions.numericOptions.limit);
        queryOptions.put("count", individualsCommandOptions.searchCommandOptions.numericOptions.count);

        return openCGAClient.getIndividualClient().search(query, queryOptions);
    }


    private QueryResponse<Individual> update() throws CatalogException, IOException {
        logger.debug("Updating individual information");

        ObjectMap params = new ObjectMap();
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.NAME.key(), individualsCommandOptions.updateCommandOptions.name);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.FAMILY.key(), individualsCommandOptions.updateCommandOptions.family);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.FATHER_ID.key(), individualsCommandOptions.updateCommandOptions.fatherId);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.MOTHER_ID.key(), individualsCommandOptions.updateCommandOptions.motherId);

        String sex = individualsCommandOptions.updateCommandOptions.sex;
        if (individualsCommandOptions.updateCommandOptions.sex != null) {
            try {
                params.put(IndividualDBAdaptor.QueryParams.SEX.key(), Individual.Sex.valueOf(sex));
            } catch (IllegalArgumentException e) {
                logger.error("{} not recognized as a proper individual sex", sex);
                return null;
            }
        }

        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.ETHNICITY.key(), individualsCommandOptions.updateCommandOptions.ethnicity);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_TAXONOMY_CODE.key(),
                individualsCommandOptions.updateCommandOptions.speciesTaxonomyCode);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_COMMON_NAME.key(),
                individualsCommandOptions.updateCommandOptions.speciesCommonName);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_SCIENTIFIC_NAME.key(),
                individualsCommandOptions.updateCommandOptions.speciesScientificName);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_NAME.key(),
                individualsCommandOptions.updateCommandOptions.populationName);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_DESCRIPTION.key(),
                individualsCommandOptions.updateCommandOptions.populationDescription);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_SUBPOPULATION.key(),
                individualsCommandOptions.updateCommandOptions.populationSubpopulation);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.KARYOTYPIC_SEX.key(),
                individualsCommandOptions.updateCommandOptions.karyotypicSex);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.LIFE_STATUS.key(),
                individualsCommandOptions.updateCommandOptions.lifeStatus);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.AFFECTATION_STATUS.key(),
                individualsCommandOptions.updateCommandOptions.affectationStatus);

        return openCGAClient.getIndividualClient().update(individualsCommandOptions.updateCommandOptions.individual, params);
    }

    private QueryResponse<Individual> delete() throws CatalogException, IOException {
        logger.debug("Deleting individual information");
        ObjectMap objectMap = new ObjectMap();
        return openCGAClient.getIndividualClient().delete(individualsCommandOptions.deleteCommandOptions.individual, objectMap);
    }

    private QueryResponse<ObjectMap> groupBy() throws CatalogException, IOException {
        logger.debug("Group by individuals");

        ObjectMap params = new ObjectMap();
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.ID.key(), individualsCommandOptions.groupByCommandOptions.id);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.NAME.key(), individualsCommandOptions.groupByCommandOptions.name);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.FATHER_ID.key(), individualsCommandOptions.groupByCommandOptions.fatherId);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.MOTHER_ID.key(), individualsCommandOptions.groupByCommandOptions.motherId);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.FAMILY.key(), individualsCommandOptions.groupByCommandOptions.family);
        params.put(IndividualDBAdaptor.QueryParams.SEX.key(), individualsCommandOptions.groupByCommandOptions.sex);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.ETHNICITY.key(), individualsCommandOptions.groupByCommandOptions.ethnicity);
        // TODO: Remove these 2 deprecated parameters in future release
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_COMMON_NAME.key(),
                individualsCommandOptions.groupByCommandOptions.species);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_NAME.key(),
                individualsCommandOptions.groupByCommandOptions.population);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_TAXONOMY_CODE.key(),
                individualsCommandOptions.groupByCommandOptions.speciesTaxonomyCode);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_COMMON_NAME.key(),
                individualsCommandOptions.groupByCommandOptions.speciesCommonName);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.SPECIES_SCIENTIFIC_NAME.key(),
                individualsCommandOptions.groupByCommandOptions.speciesScientificName);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_NAME.key(),
                individualsCommandOptions.groupByCommandOptions.populationName);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_DESCRIPTION.key(),
                individualsCommandOptions.groupByCommandOptions.populationDescription);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.POPULATION_SUBPOPULATION.key(),
                individualsCommandOptions.groupByCommandOptions.populationSubpopulation);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.KARYOTYPIC_SEX.key(),
                individualsCommandOptions.groupByCommandOptions.karyotypicSex);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.LIFE_STATUS.key(),
                individualsCommandOptions.groupByCommandOptions.lifeStatus);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.AFFECTATION_STATUS.key(),
                individualsCommandOptions.groupByCommandOptions.affectationStatus);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.VARIABLE_SET_ID.key(),
                individualsCommandOptions.groupByCommandOptions.variableSetId);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.ANNOTATION.key(),
                individualsCommandOptions.groupByCommandOptions.annotation);
        params.putIfNotEmpty(IndividualDBAdaptor.QueryParams.ANNOTATION_SET_NAME.key(),
                individualsCommandOptions.groupByCommandOptions.annotationSetName);

        return openCGAClient.getIndividualClient().groupBy(
                individualsCommandOptions.groupByCommandOptions.study, individualsCommandOptions.groupByCommandOptions.fields, params);
    }

}