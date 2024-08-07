package dev.openfeature.providers.azure;

import com.azure.spring.cloud.feature.management.FeatureManager;
import dev.openfeature.sdk.*;

/**
 * OpenFeature feature provider using Azure App Configuration
 */
public class AzureFeatureProvider implements FeatureProvider {

    private final FeatureManager featureManager;

    public AzureFeatureProvider(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    @Override
    public Metadata getMetadata() {
        return () -> "Azure App Configuration OpenFeature provider";
    }

    @Override
    public ProviderEvaluation<Boolean> getBooleanEvaluation(String key, Boolean defaultValue, EvaluationContext ctx) {
        if(! featureManager.getAllFeatureNames().contains(key)) {
            // feature manager doesn't support the required key, so using the default value that is provided
            return ProviderEvaluation.<Boolean>builder().value(defaultValue).reason(Reason.DEFAULT.name()).build();
        }
        var isEnabled = featureManager.isEnabled(key);

        var resultBuilder = ProviderEvaluation.<Boolean>builder()
                .reason(Reason.STATIC.name())
                .value(isEnabled);

        return resultBuilder.build();
    }

    @Override
    public ProviderEvaluation<String> getStringEvaluation(String key, String defaultValue, EvaluationContext ctx) {
        // azure app configuration does not support String values, so using the default value that is provided
        return ProviderEvaluation.<String>builder().value(defaultValue).reason(Reason.DEFAULT.name()).build();
    }

    @Override
    public ProviderEvaluation<Integer> getIntegerEvaluation(String key, Integer defaultValue, EvaluationContext ctx) {
        // azure app configuration does not support Integer values, so using the default value that is provided
        return ProviderEvaluation.<Integer>builder().value(defaultValue).reason(Reason.DEFAULT.name()).build();
    }

    @Override
    public ProviderEvaluation<Double> getDoubleEvaluation(String key, Double defaultValue, EvaluationContext ctx) {
        // azure app configuration does not support Double values, so using the default value that is provided
        return ProviderEvaluation.<Double>builder().value(defaultValue).reason(Reason.DEFAULT.name()).build();
    }

    @Override
    public ProviderEvaluation<Value> getObjectEvaluation(String key, Value defaultValue, EvaluationContext ctx) {
        // azure app configuration does not support Value values, so using the default value that is provided
        return ProviderEvaluation.<Value>builder().value(defaultValue).reason(Reason.DEFAULT.name()).build();
    }
}
