package com.javacodetest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javacodetest.util.GlobalConstants;

@Service
public class CustomerDetailsServiceImpl implements CustomerDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerDetailsServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;

	@Override
	public Map<String, Object> getCustomerDetails(Integer CustomerId) {
		logger.info("Customer Details Service -- Inside getCustomerDetails Method");
		Map<String, Object> responseObject = new HashMap<String, Object>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			List<String> stringList = new ArrayList<>();
			String data1 = "https://6466e9a7ba7110b663ab51f2.mockapi.io/api/v1/pack1";

			List pack1List = restTemplate.getForObject(data1, List.class);
			logger.info("Customer Details Service -- Successfully Fetched Pack1 Data using rest Template");

			String data2 = "https://6466e9a7ba7110b663ab51f2.mockapi.io/api/v1/pack2";

			List pack2List = restTemplate.getForObject(data2, List.class);
			logger.info("Customer Details Service -- Successfully Fetched Pack2 Data using rest Template");

			for (Object pack1 : pack1List) {
				responseObject = new HashMap<String, Object>();
				stringList = new ArrayList<>();
				Map<String, Object> pack1Map = objectMapper.convertValue(pack1,
						new TypeReference<Map<String, Object>>() {
						});
				logger.info("Customer Details Service -- Successfully converted Pack1 Object");

				if (pack1Map.get(GlobalConstants.CustomerId).equals(CustomerId)) {

					responseObject.put(GlobalConstants.ID, pack1Map.get(GlobalConstants.ID));

					responseObject.put(GlobalConstants.CustomerId, pack1Map.get(GlobalConstants.CustomerId));

					List pack1DataList = objectMapper.convertValue(pack1Map.get(GlobalConstants.PACKDATA),
							new TypeReference<List>() {
							});
					logger.info("Customer Details Service -- Successfully converted pack_data into list");

					for (Object pack1Data : pack1DataList) {
						Map<String, Object> pack1DataMap = objectMapper.convertValue(pack1Data,
								new TypeReference<Map<String, Object>>() {
								});

						logger.info("Customer Details Service -- Successfully converted pack_data into map");

						String packDataString = pack1DataMap.get(GlobalConstants.INGREDIENT) + " "
								+ pack1DataMap.get(GlobalConstants.QUANTITY) + " "
								+ pack1DataMap.get(GlobalConstants.UNIT);

						stringList.add(packDataString);

					}

					responseObject.put(GlobalConstants.PACK1, stringList);

					stringList = new ArrayList<>();
					for (Object pack2 : pack2List) {

						Map<String, Object> pack2Map = objectMapper.convertValue(pack2,
								new TypeReference<Map<String, Object>>() {
								});
						logger.info("Customer Details Service -- Successfully converted Pack2 Object");

						if (pack2Map.get(GlobalConstants.CustomerId).equals(pack1Map.get(GlobalConstants.CustomerId))) {

							List pack2DataList = objectMapper.convertValue(pack2Map.get(GlobalConstants.PACKDATA),
									new TypeReference<List>() {
									});
							logger.info("Customer Details Service -- Successfully converted pack_data into list");

							for (Object pack2Data : pack2DataList) {
								Map<String, Object> pack2DataMap = objectMapper.convertValue(pack2Data,
										new TypeReference<Map<String, Object>>() {
										});
								logger.info("Customer Details Service -- Successfully converted pack_data into map");

								String packDataString = pack2DataMap.get(GlobalConstants.INGREDIENT) + " "
										+ pack2DataMap.get(GlobalConstants.QUANTITY) + " "
										+ pack2DataMap.get(GlobalConstants.UNIT);

								logger.info(
										"Customer Details Service -- Successfully appended all the details to be populated in pack data list");
								stringList.add(packDataString);

							}

							responseObject.put(GlobalConstants.PACK2, stringList);

						}

					}
					break;
				}else {
					logger.error("CustomerDetailsService -- No Customer details Exit ");
					responseObject.put(GlobalConstants.CustomerId, CustomerId);
					responseObject.put("message", "No Customer details Exist");
				}

			}

		} catch (Exception e) {

			logger.error("CustomerDetailsService -- Exception occured due to " + e.getMessage());
		}

		return responseObject;
	}

}
