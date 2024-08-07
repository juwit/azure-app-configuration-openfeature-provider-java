package dev.openfeature.providers.azure;

import com.azure.spring.cloud.feature.management.FeatureManager;
import dev.openfeature.sdk.Reason;
import dev.openfeature.sdk.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AzureFeatureProviderTest {

    @InjectMocks
    private AzureFeatureProvider azureFeatureProvider;

    @Mock
    private FeatureManager featureManager;

    @Test
    void getMetadata_isNotNull() {
        assertThat(azureFeatureProvider.getMetadata()).isNotNull();
    }

    @Test
    void getBooleanEvaluation_returnsDefault_whenFeatureDoesNotExists() {
        var falseEvaluationResult = azureFeatureProvider.getBooleanEvaluation("no-such-feature", false, null);

        assertThat(falseEvaluationResult).isNotNull();
        assertThat(falseEvaluationResult.getValue()).isFalse();
        assertThat(falseEvaluationResult.getReason()).isEqualTo(Reason.DEFAULT.toString());

        var trueEvaluationResult = azureFeatureProvider.getBooleanEvaluation("no-such-feature", true, null);

        assertThat(trueEvaluationResult).isNotNull();
        assertThat(trueEvaluationResult.getValue()).isTrue();
        assertThat(trueEvaluationResult.getReason()).isEqualTo(Reason.DEFAULT.toString());
    }

    @Test
    void getBooleanEvaluation_delegatesToAzureFeatureManager_forEnabledFeature() {
        when(featureManager.getAllFeatureNames()).thenReturn(Set.of("enabled-feature"));
        when(featureManager.isEnabled("enabled-feature")).thenReturn(true);

        var evaluationResult = azureFeatureProvider.getBooleanEvaluation("enabled-feature", false, null);

        assertThat(evaluationResult).isNotNull();
        assertThat(evaluationResult.getValue()).isTrue();
        assertThat(evaluationResult.getReason()).isEqualTo(Reason.STATIC.toString());
    }

    @Test
    void getBooleanEvaluation_delegatesToAzureFeatureManager_forDisabledFeature() {
        when(featureManager.getAllFeatureNames()).thenReturn(Set.of("disabled-feature"));
        when(featureManager.isEnabled("disabled-feature")).thenReturn(false);

        var evaluationResult = azureFeatureProvider.getBooleanEvaluation("disabled-feature", true, null);

        assertThat(evaluationResult).isNotNull();
        assertThat(evaluationResult.getValue()).isFalse();
        assertThat(evaluationResult.getReason()).isEqualTo(Reason.STATIC.toString());
    }

    @Test
    void getStringEvaluation_alwaysReturnsDefault() {
        var evaluationResult = azureFeatureProvider.getStringEvaluation("some-feature", "default-value", null);

        assertThat(evaluationResult).isNotNull();
        assertThat(evaluationResult.getValue()).isEqualTo("default-value");
        assertThat(evaluationResult.getReason()).isEqualTo(Reason.DEFAULT.toString());
    }

    @Test
    void getIntegerEvaluation_alwaysReturnsDefault() {
        var evaluationResult = azureFeatureProvider.getIntegerEvaluation("some-feature", 66, null);

        assertThat(evaluationResult).isNotNull();
        assertThat(evaluationResult.getValue()).isEqualTo(66);
        assertThat(evaluationResult.getReason()).isEqualTo(Reason.DEFAULT.toString());
    }

    @Test
    void getDoubleEvaluation_alwaysReturnsDefault() {
        var evaluationResult = azureFeatureProvider.getDoubleEvaluation("some-feature", 12.0, null);

        assertThat(evaluationResult).isNotNull();
        assertThat(evaluationResult.getValue()).isEqualTo(12.0);
        assertThat(evaluationResult.getReason()).isEqualTo(Reason.DEFAULT.toString());
    }

    @Test
    void getObjectEvaluation_alwaysReturnsDefault() {
        var evaluationResult = azureFeatureProvider.getObjectEvaluation("some-feature", Value.objectToValue(List.of("listItem")), null);

        assertThat(evaluationResult).isNotNull();
        assertThat(evaluationResult.getValue().isList()).isTrue();
        assertThat(evaluationResult.getValue().asList()).containsExactly(Value.objectToValue("listItem"));
        assertThat(evaluationResult.getReason()).isEqualTo(Reason.DEFAULT.toString());
    }

}