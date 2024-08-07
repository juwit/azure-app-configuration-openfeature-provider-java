package dev.openfeature.providers.azure;

import com.azure.spring.cloud.feature.management.FeatureManager;
import dev.openfeature.sdk.OpenFeatureAPI;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class AzureFeatureProviderAutoconfiguration {

    @Bean
    public OpenFeatureAPI openFeatureAPI(AzureFeatureProvider azureFeatureProvider) {
        OpenFeatureAPI openFeatureAPI = OpenFeatureAPI.getInstance();

        openFeatureAPI.setProviderAndWait(azureFeatureProvider);

        return openFeatureAPI;
    }

    @Bean
    public AzureFeatureProvider azureFeatureProvider(FeatureManager featureManager) {
        return new AzureFeatureProvider(featureManager);
    }

}
