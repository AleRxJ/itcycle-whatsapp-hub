package com.itcycle.whatsapp.domain.port.out;

import java.util.Map;

/**
 * N8nClientPort - output port for n8n workflow automation integration.
 * This interface defines the contract for triggering n8n workflows.
 * Implementations are provided by the adapters-out-external module.
 */
public interface N8nClientPort {
    
    /**
     * Trigger an n8n workflow via webhook
     * @param flowKey identifier for the n8n workflow (e.g., "lead_created", "message_received")
     * @param payload data to send to the workflow
     * @return response from n8n (could be execution ID or confirmation)
     */
    String triggerFlow(String flowKey, Map<String, Object> payload);
    
    /**
     * Trigger a workflow and wait for synchronous response
     * @param flowKey workflow identifier
     * @param payload data to send
     * @return response data from n8n workflow
     */
    Map<String, Object> triggerFlowSync(String flowKey, Map<String, Object> payload);
}
