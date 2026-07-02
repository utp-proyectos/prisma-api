package pe.edu.utp.prisma_api.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.prisma_api.common.enums.WsAction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WsResponse<T> {
    WsAction action;
    T payload;

}
