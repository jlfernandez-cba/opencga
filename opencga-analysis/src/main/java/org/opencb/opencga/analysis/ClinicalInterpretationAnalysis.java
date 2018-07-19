/*
 * Copyright 2015-2017 OpenCB
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

package org.opencb.opencga.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

<<<<<<< HEAD
=======
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
>>>>>>> origin/playground
import org.apache.commons.lang3.StringUtils;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.commons.datastore.core.Query;
import org.opencb.commons.datastore.core.QueryOptions;
import org.opencb.commons.datastore.core.QueryResult;
import org.opencb.opencga.catalog.db.api.DiseasePanelDBAdaptor;
import org.opencb.opencga.catalog.exceptions.CatalogException;
<<<<<<< HEAD
import org.opencb.opencga.catalog.managers.CatalogManager;
import org.opencb.opencga.core.common.TimeUtils;
import org.opencb.opencga.core.config.Configuration;
import org.opencb.opencga.core.models.DiseasePanel;
import org.opencb.opencga.core.models.User;
import org.opencb.opencga.core.models.clinical.Analyst;
import org.opencb.opencga.core.models.clinical.Interpretation;
import org.opencb.opencga.core.models.clinical.ReportedEvent;
import org.opencb.opencga.core.models.clinical.ReportedEvent.VariantClassification;
import org.opencb.opencga.core.models.clinical.ReportedVariant;
import org.opencb.opencga.core.results.VariantQueryResult;
import org.opencb.opencga.storage.core.StorageEngineFactory;
import org.opencb.opencga.storage.core.config.StorageConfiguration;
=======
import org.opencb.opencga.catalog.managers.ClinicalAnalysisManager;
import org.opencb.opencga.catalog.managers.IndividualManager;
import org.opencb.opencga.core.common.TimeUtils;
import org.opencb.opencga.core.models.ClinicalAnalysis;
import org.opencb.opencga.core.models.ClinicalAnalysis.Type;
import org.opencb.opencga.core.models.DiseasePanel;
import org.opencb.opencga.core.models.Family;
import org.opencb.opencga.core.models.Individual;
import org.opencb.opencga.core.models.User;
import org.opencb.opencga.core.models.clinical.Analyst;
import org.opencb.opencga.core.models.clinical.Interpretation;
import org.opencb.opencga.core.models.clinical.ReportedVariant;
import org.opencb.opencga.core.results.VariantQueryResult;
import org.opencb.opencga.storage.core.StorageEngineFactory;
>>>>>>> origin/playground
import org.opencb.opencga.storage.core.exceptions.StorageEngineException;
import org.opencb.opencga.storage.core.manager.variant.VariantStorageManager;
import org.opencb.opencga.storage.core.variant.adaptors.VariantQueryParam;

public class ClinicalInterpretationAnalysis extends OpenCgaAnalysis {
    private Interpretation interpretation;

    private final String sessionId;
    private final String studyStr;

    private final String panelId;
    private final String panelVersion;
    private final String sampleId;

<<<<<<< HEAD
    private final boolean searchForVUS;
    private final boolean searchForUF;
=======
    private final boolean lookForVUS;
    private final boolean lookForUnexpectedFindings;
>>>>>>> origin/playground

    private final String interpretationId;
    private final String interpretationName;

    // minimal constructor
    public ClinicalInterpretationAnalysis(
<<<<<<< HEAD
            Configuration configuration,
            StorageConfiguration storageConfiguration,
            String panelId,
            String sampleId,
            String sessionId
            ) throws CatalogException {
        this(configuration, storageConfiguration, null, panelId, null, sampleId, null, null, true, true, sessionId);
=======
            String opencgaHome,
            String panelId,
            String sampleId,
            String sessionId
            ) {
        this(opencgaHome, null, panelId, null, sampleId, null, null, true, true, sessionId);
>>>>>>> origin/playground
    }

    // full constructor
    public ClinicalInterpretationAnalysis(
<<<<<<< HEAD
            Configuration configuration,
            StorageConfiguration storageConfiguration,
=======
            String opencgaHome,
>>>>>>> origin/playground
            String studyStr,
            String panelId,
            String panelVersion,
            String sampleId,
            String interpretationId,
            String interpretationName,
            boolean lookForVUS,
            boolean lookForUnexpectedFindings,
<<<<<<< HEAD
            String sessionId) throws CatalogException {
        super(configuration, storageConfiguration);
=======
            String sessionId) {
        super(opencgaHome);
>>>>>>> origin/playground
        this.sessionId = sessionId;
        this.studyStr = studyStr;
        this.panelId = panelId;
        this.panelVersion = panelVersion;
        this.sampleId = sampleId;
        this.interpretationId = interpretationId;
        this.interpretationName = interpretationName;
<<<<<<< HEAD
        this.searchForVUS = lookForVUS;
        this.searchForUF = lookForUnexpectedFindings;
    }

    private DiseasePanel getDiseasePanel() throws CatalogException {

=======
        this.lookForVUS = lookForVUS;
        this.lookForUnexpectedFindings = lookForUnexpectedFindings;
    }

    private DiseasePanel getDiseasePanel() throws CatalogException {
>>>>>>> origin/playground
        Query panelQuery = new Query();
        panelQuery.put(DiseasePanelDBAdaptor.QueryParams.ID.key(), panelId);
        if (StringUtils.isNotBlank(panelVersion)) {
            panelQuery.put(DiseasePanelDBAdaptor.QueryParams.VERSION.key(), panelVersion);
        }
        QueryResult<DiseasePanel> panelResult = catalogManager.getDiseasePanelManager().get(studyStr, panelQuery, QueryOptions.empty(), sessionId);
        return panelResult.first();
    }

    private List<ReportedVariant> getDiagnosticVariants(VariantStorageManager variantManager, DiseasePanel diseasePanel, String sampleId) throws CatalogException, StorageEngineException, IOException {
        List<DiseasePanel.VariantPanel> variants = diseasePanel.getVariants();
        Query variantQuery = new Query();
        variantQuery.put(VariantQueryParam.ID.key(), variants);
        variantQuery.put(VariantQueryParam.SAMPLE.key(), sampleId);
        VariantQueryResult<Variant> variantQueryResult = variantManager.get(variantQuery, QueryOptions.empty(), sessionId);
        List<ReportedVariant> reportedVariants = variantQueryResult.getResult().stream().map(variant -> {
            ReportedVariant reportedVariant = new ReportedVariant(variant.getImpl());
<<<<<<< HEAD
            ReportedEvent event = new ReportedEvent();
            event
                // .setId("id")
                // .setPhenotype("phenotype")
                .setVariantClassification(VariantClassification.PATHOGENIC_VARIANT)
                .setScore(1.0)
                ;
            reportedVariant.getReportedEvents().add(event);
=======
>>>>>>> origin/playground
            // reportedEvents: List<ReportedEvent>
            // comments: List<String>
            // attributes: Map<String, Object>
            return reportedVariant;
        }).collect(Collectors.toList());
        return reportedVariants;
    }

    private List<ReportedVariant> getVUS(VariantStorageManager variantManager, DiseasePanel diseasePanel, String sampleId) throws CatalogException, StorageEngineException, IOException {
<<<<<<< HEAD
        // TODO: implement this (working on specs)
=======
>>>>>>> origin/playground
        Query query = new Query();
        query.put(VariantQueryParam.GENE.key(), diseasePanel.getGenes());
        query.put(VariantQueryParam.SAMPLE.key(), sampleId);
        query.put(VariantQueryParam.ANNOT_BIOTYPE.key(), "protein_coding");
        // ...
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.put(QueryOptions.LIMIT, 1000);
        VariantQueryResult<Variant> queryResult = variantManager.get(query, queryOptions, sessionId);
        List<ReportedVariant> reportedVariants = queryResult.getResult().stream().map(variant -> {
            ReportedVariant reportedVariant = new ReportedVariant(variant.getImpl());
<<<<<<< HEAD
            ReportedEvent event = new ReportedEvent();
            event
                // .setId("id")
                // .setPhenotype("phenotype")
                .setVariantClassification(VariantClassification.VARIANT_OF_UNKNOWN_CLINICAL_SIGNIFICANCE)
                .setScore(0.5)
                ;
            reportedVariant.getReportedEvents().add(event);
=======
>>>>>>> origin/playground
            return reportedVariant;
        }).collect(Collectors.toList());
        return reportedVariants;
    }

    private List<ReportedVariant> getUnexpectedFindings(VariantStorageManager variantManager, String sampleId) {
<<<<<<< HEAD
        // TODO: implement this (working on specs)
        return new ArrayList<ReportedVariant>();
    }

    private Interpretation createGenericInterpretation(CatalogManager catalogManager) throws CatalogException {
        interpretation = new Interpretation();
        interpretation.setCreationDate(TimeUtils.getTime());
        // .setSoftware()
        // .setVersions(versions)
        // .setFilters(filters)
        // .setComments(comments)
        // .setAttributes(attributes)

        if (StringUtils.isNotEmpty(interpretationId)) {
            interpretation.setId(interpretationId);
        }
        if (StringUtils.isNotEmpty(interpretationName)) {
            interpretation.setName(interpretationName);
        }
        // analyst
        String userId = catalogManager.getUserManager().getUserId(sessionId);
        User user = catalogManager.getUserManager().get(userId,  QueryOptions.empty(), sessionId).first();
        interpretation.setAnalyst(new Analyst(user.getName(), user.getEmail(), user.getOrganization()));
        return interpretation;
    }
=======
        return new ArrayList<ReportedVariant>();
    }

>>>>>>> origin/playground

    public void execute() throws Exception {
        if (StringUtils.isEmpty(this.panelId)) {
            // TODO: proper error management
            logger.error("No disease panel provided");
            return;
        }
        DiseasePanel diseasePanel = getDiseasePanel();

        StorageEngineFactory storageEngineFactory = StorageEngineFactory.get(storageConfiguration);
        VariantStorageManager variantManager = new VariantStorageManager(catalogManager, storageEngineFactory);

<<<<<<< HEAD
        List<ReportedVariant> reportedVariants = getDiagnosticVariants(variantManager, diseasePanel, sampleId);
        if (this.searchForVUS) {
            reportedVariants.addAll(getVUS(variantManager, diseasePanel, sampleId));
        }
        if (this.searchForUF) {
=======

        List<ReportedVariant> reportedVariants = getDiagnosticVariants(variantManager, diseasePanel, sampleId);
        if (this.lookForVUS) {
            reportedVariants.addAll(getVUS(variantManager, diseasePanel, sampleId));
        }
        if (this.lookForUnexpectedFindings) {
>>>>>>> origin/playground
            reportedVariants.addAll(getUnexpectedFindings(variantManager, sampleId));
        }

        // setup result
<<<<<<< HEAD
        createGenericInterpretation(catalogManager)
            .setReportedVariants(reportedVariants);
        interpretation
            .setDescription("Automatic interpretation based on panel " + diseasePanel.getId())
            .setPanel(diseasePanel)
            .setReportedVariants(reportedVariants);
            ;
=======
        interpretation = (new Interpretation());
        interpretation
            .setDescription("Automatic interpretation based on panel " + diseasePanel.getId())
            .setPanel(diseasePanel)
            // .setSoftware()
            // .setVersions(versions)
            // .setFilters(filters)
            .setCreationDate(TimeUtils.getTime())
            // .setComments(comments)
            // .setAttributes(attributes)
            .setReportedVariants(reportedVariants);
            ;
        if (StringUtils.isNotEmpty(interpretationId)) {
            interpretation.setId(interpretationId);
        }
        if (StringUtils.isNotEmpty(interpretationName)) {
            interpretation.setName(interpretationName);
        }
        // analyst
        String userId = catalogManager.getUserManager().getUserId(sessionId);
        User user = catalogManager.getUserManager().get(userId,  QueryOptions.empty(), sessionId).first();
        interpretation.setAnalyst(new Analyst(user.getName(), user.getEmail(), user.getOrganization()));
>>>>>>> origin/playground
    }

    public Interpretation getInterpretation() {
        return interpretation;
    }
}
